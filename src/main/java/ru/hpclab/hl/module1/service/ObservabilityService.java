package ru.hpclab.hl.module1.service;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;

//      long start = System.currentTimeMillis();
//      try
//      {
//        // код
//      }
//      finally
//      {
//        ObservabilityService.recordTiming("metric", System.currentTimeMillis() - start);
//      }

public final class ObservabilityService {
    private static final ConcurrentMap<String, TimingStats> metrics = new ConcurrentHashMap<>();
    private static final long CLEANUP_THRESHOLD_MS = TimeUnit.MINUTES.toMillis(5);
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final long PRINT_INTERVAL_SECONDS = 30;

    static {
        // Запускаем периодический вывод метрик
        scheduler.scheduleAtFixedRate(
                ObservabilityService::printMetrics,
                PRINT_INTERVAL_SECONDS,
                PRINT_INTERVAL_SECONDS,
                TimeUnit.SECONDS
        );

        // Добавляем shutdown hook для корректного завершения
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }));
    }
    // Новый метод для вывода метрик
    private static void printMetrics() {
        MetricsSnapshot snapshot = getMetricsAndClean();
        System.out.println("\n=== Metrics Report at " + snapshot.timestamp + " ===");

        printMetricGroup("Last 10 seconds", snapshot.last10s);
        printMetricGroup("Last 30 seconds", snapshot.last30s);
        printMetricGroup("Last 1 minute", snapshot.last1m);
    }
    private static void printMetricGroup(String title, Map<String, MetricStats> stats) {
        System.out.println("\n" + title + ":");
        if (stats.isEmpty()) {
            System.out.println("  No metrics recorded");
            return;
        }

        stats.forEach((name, metric) -> {
            System.out.printf("  %s: count=%d, avg=%.2fms, min=%dms, max=%dms%n",
                    name, metric.count, metric.avgMs, metric.minMs, metric.maxMs);
        });
    }
    // Thread-safe запись метрик
    public static void recordTiming(String metricName, long durationMs) {
        metrics.computeIfAbsent(metricName, k -> new TimingStats()).addRecord(durationMs);
    }

    // Атомарно: статистика + очистка
    public static synchronized MetricsSnapshot getMetricsAndClean() {
        MetricsSnapshot snapshot = new MetricsSnapshot(
                collectStats(10_000),
                collectStats(30_000),
                collectStats(60_000)
        );
        cleanOldData();
        return snapshot;
    }

    private static Map<String, MetricStats> collectStats(long periodMs) {
        long cutoff = System.currentTimeMillis() - periodMs;
        return metrics.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().calculateStats(cutoff)
                ));
    }

    private static void cleanOldData() {
        long oldestAllowed = System.currentTimeMillis() - CLEANUP_THRESHOLD_MS;
        metrics.values().forEach(stats -> stats.removeOlderThan(oldestAllowed));
        metrics.entrySet().removeIf(e -> e.getValue().isEmpty());
    }

    // DTO для ответа
    public static final class MetricsSnapshot {
        public final Map<String, MetricStats> last10s;
        public final Map<String, MetricStats> last30s;
        public final Map<String, MetricStats> last1m;
        public final String timestamp;

        public MetricsSnapshot(Map<String, MetricStats> last10s,
                               Map<String, MetricStats> last30s,
                               Map<String, MetricStats> last1m) {
            this(last10s, last30s, last1m, Instant.now());
        }

        public MetricsSnapshot(Map<String, MetricStats> last10s,
                               Map<String, MetricStats> last30s,
                               Map<String, MetricStats> last1m,
                               Instant instant) {
            this.last10s = Collections.unmodifiableMap(last10s);
            this.last30s = Collections.unmodifiableMap(last30s);
            this.last1m = Collections.unmodifiableMap(last1m);
            this.timestamp = DateTimeFormatter.ISO_INSTANT.format(instant);
        }
    }

    private static final class TimingStats {
        private final ConcurrentLinkedDeque<TimingRecord> records = new ConcurrentLinkedDeque<>();

        void addRecord(long durationMs) {
            records.add(new TimingRecord(System.currentTimeMillis(), durationMs));
        }

        MetricStats calculateStats(long cutoff) {
            LongSummaryStatistics stats = records.stream()
                    .filter(r -> r.timestamp >= cutoff)
                    .collect(Collectors.summarizingLong(r -> r.duration));
            return new MetricStats(stats.getCount(), stats.getAverage(), stats.getMin(), stats.getMax());
        }

        void removeOlderThan(long timestamp) {
            records.removeIf(r -> r.timestamp < timestamp);
        }

        boolean isEmpty() {
            return records.isEmpty();
        }
    }

    private static final class TimingRecord {
        final long timestamp;
        final long duration;

        TimingRecord(long timestamp, long duration) {
            this.timestamp = timestamp;
            this.duration = duration;
        }
    }

    public static final class MetricStats {
        public final long count;
        public final double avgMs;
        public final long minMs;
        public final long maxMs;

        public MetricStats(long count, double avgMs, long minMs, long maxMs) {
            this.count = count;
            this.avgMs = avgMs;
            this.minMs = minMs;
            this.maxMs = maxMs;
        }
    }
}
