package com.vigilan.backend.service.impl;

import com.vigilan.backend.dto.queue.MonitoringJobMessage;
import com.vigilan.backend.dto.response.MonitoringJobResponseDTO;
import com.vigilan.backend.entity.MonitoringJob;
import com.vigilan.backend.repository.MonitoringJobRepository;
import com.vigilan.backend.service.MonitoringService;
import com.vigilan.backend.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MonitoringServiceImpl implements MonitoringService {

    private final MonitoringJobRepository jobRepository;
    private final QueueService queueService;

    @Override
    public MonitoringJobResponseDTO startMonitoring(Long videoId, String mode) {

        MonitoringJob job = new MonitoringJob();
        job.setVideoId(videoId);
        job.setMode(mode);
        job.setStatus("PENDING");
        job.setProgress(0);
        job.setStartedAt(LocalDateTime.now());
        job.setFinishedAt(null);

        MonitoringJob saved = jobRepository.save(job);

        MonitoringJobMessage message = new MonitoringJobMessage(
                saved.getId(),
                saved.getVideoId(),
                saved.getMode()
        );

        queueService.sendMonitoringJob(message);

        return new MonitoringJobResponseDTO(
                saved.getId(),
                saved.getStatus(),
                saved.getProgress()
        );
    }

    @Override
    public MonitoringJobResponseDTO getStatus(Long jobId) {

        MonitoringJob job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Monitoring job not found"));

        return new MonitoringJobResponseDTO(
                job.getId(),
                job.getStatus(),
                job.getProgress()
        );
    }
}