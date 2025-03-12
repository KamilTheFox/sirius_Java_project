package ru.hpclab.hl.module1.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

public class StatisticsService {


    @Value("${statisticsservice.infostring:lines}")
    private String infoString;

    final int delay;

    private final UserService userService;

    public StatisticsService(int delay, UserService userService) {
        this.delay = delay;
        this.userService = userService;
    }

    @Async(value = "applicationTaskExecutor")
    @Scheduled(fixedRateString = "${fixedRate.in.milliseconds}")
    public void scheduleFixedRateTaskAsync() throws InterruptedException {
        System.out.println(
                Thread.currentThread().getName() + " - Fixed rate task async - "+ delay + " - " + infoString + " - "
                        + userService.getAllUsers().size());
        Thread.sleep(delay);
    }
}
