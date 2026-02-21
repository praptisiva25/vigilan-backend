package com.vigilan.backend.controller;

import com.vigilan.backend.entity.MonitoringJob;
import com.vigilan.backend.repository.MonitoringJobRepository;
import com.vigilan.backend.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/monitoring")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")

public class MonitoringController {

    private final MonitoringJobRepository jobRepository;
    private final MonitoringService monitoringService;


    @PostMapping("/start/{videoId}")
    public MonitoringJob startMonitoring(
            @PathVariable Long videoId,
            @RequestParam String mode) {

        MonitoringJob job = new MonitoringJob();
        job.setVideoId(videoId);
        job.setMode(mode);
        job.setStatus("PENDING");
        job.setProgress(0);
        job.setStartedAt(LocalDateTime.now());

        jobRepository.save(job);

        monitoringService.processVideo(job);

        return job;
    }

    @GetMapping("/status/{jobId}")
    public MonitoringJob getStatus(@PathVariable Long jobId) {
        return jobRepository.findById(jobId).orElseThrow();
    }
}
