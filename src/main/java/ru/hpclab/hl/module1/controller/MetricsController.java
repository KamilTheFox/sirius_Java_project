package ru.hpclab.hl.module1.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hpclab.hl.module1.service.*;

@RestController
@RequestMapping("/metrics")
public class MetricsController
{
    @GetMapping
    public ObservabilityService.MetricsSnapshot getMetrics()
    {
        return ObservabilityService.getMetricsAndClean();
    }
}
