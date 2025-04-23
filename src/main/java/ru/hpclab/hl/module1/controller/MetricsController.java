package ru.hpclab.hl.module1.controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.hpclab.hl.module1.service.*;

public class MetricsController
{
    @GetMapping("/metrics")
    public ObservabilityService.MetricsSnapshot getMetrics()
    {
        return ObservabilityService.getMetricsAndClean();
    }
}
