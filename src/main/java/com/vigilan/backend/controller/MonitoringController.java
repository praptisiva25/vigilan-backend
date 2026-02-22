package com.vigilan.backend.controller;

import com.vigilan.backend.dto.response.MonitoringJobResponseDTO;
import com.vigilan.backend.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/monitoring")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class MonitoringController {

    private final MonitoringService monitoringService;

    @PostMapping("/start/{videoId}")
    public MonitoringJobResponseDTO startMonitoring(
            @PathVariable Long videoId,
            @RequestParam String mode) {

        return monitoringService.startMonitoring(videoId, mode);
    }

    @GetMapping("/status/{jobId}")
    public MonitoringJobResponseDTO getStatus(
            @PathVariable Long jobId) {

        return monitoringService.getStatus(jobId);
    }
}