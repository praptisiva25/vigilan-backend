package com.vigilan.backend.controller;

import com.vigilan.backend.dto.request.MonitoringJobUpdateRequestDTO;
import com.vigilan.backend.dto.response.MonitoringContextResponseDTO;
import com.vigilan.backend.dto.response.MonitoringJobResponseDTO;
import com.vigilan.backend.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monitoring")
@RequiredArgsConstructor
public class MonitoringController {

    private final MonitoringService monitoringService;

    // Start Monitoring
    @PostMapping("/start/{videoId}")
    public MonitoringJobResponseDTO startMonitoring(
            @PathVariable Long videoId,
            @RequestBody List<Long> zoneIds) {

        return monitoringService.startMonitoring(videoId, zoneIds);
    }

    // Get Job Status
    @GetMapping("/status/{jobId}")
    public MonitoringJobResponseDTO getStatus(@PathVariable Long jobId) {
        return monitoringService.getStatus(jobId);
    }

    // Get Full Context (Python calls this)
    @GetMapping("/{jobId}/context")
    public MonitoringContextResponseDTO getMonitoringContext(@PathVariable Long jobId) {
        return monitoringService.getMonitoringContext(jobId);
    }

    // Update Job Progress / Status (Python calls this)
    @PatchMapping("/{jobId}")
    public void updateMonitoringJob(
            @PathVariable Long jobId,
            @RequestBody MonitoringJobUpdateRequestDTO request) {

        monitoringService.updateMonitoringJob(jobId, request);
    }
}