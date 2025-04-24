package ru.hpclab.hl.module1.controller;
import org.springframework.web.bind.annotation.*;
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
