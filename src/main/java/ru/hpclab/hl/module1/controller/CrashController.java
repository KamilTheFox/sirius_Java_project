package ru.hpclab.hl.module1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrashController {
    @GetMapping("/crash")
    public void crashApp()
    {
        System.exit(1);
    }
}
