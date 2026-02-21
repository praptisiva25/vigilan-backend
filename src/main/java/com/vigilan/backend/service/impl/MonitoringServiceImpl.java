package com.vigilan.backend.service.impl;

import com.vigilan.backend.dto.response.MonitoringJobResponseDTO;
import com.vigilan.backend.entity.MonitoringJob;
import com.vigilan.backend.repository.MonitoringJobRepository;
import com.vigilan.backend.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MonitoringServiceImpl implements MonitoringService {

    private static final Logger logger =
            LoggerFactory.getLogger(MonitoringServiceImpl.class);

    private final MonitoringJobRepository jobRepository;

    @Override
    public MonitoringJobResponseDTO startMonitoring(Long videoId, String mode) {

        MonitoringJob job = new MonitoringJob();
        job.setVideoId(videoId);
        job.setMode(mode);
        job.setStatus("PENDING");
        job.setProgress(0);
        job.setStartedAt(LocalDateTime.now());

        MonitoringJob saved = jobRepository.save(job);

        processVideo(saved);

        return mapToDTO(saved);
    }

    @Override
    public MonitoringJobResponseDTO getStatus(Long jobId) {
        MonitoringJob job = jobRepository.findById(jobId).orElseThrow();
        return mapToDTO(job);
    }

    @Async
    public void processVideo(MonitoringJob job) {

        job.setStatus("RUNNING");
        jobRepository.save(job);

        for (int i = 1; i <= 100; i++) {

            try {
                Thread.sleep("LIVE".equals(job.getMode()) ? 100 : 10);
            } catch (InterruptedException e) {
                logger.error("Monitoring error", e);
            }

            job.setProgress(i);
            jobRepository.save(job);
        }

        job.setStatus("COMPLETED");
        job.setFinishedAt(LocalDateTime.now());
        jobRepository.save(job);
    }

    private MonitoringJobResponseDTO mapToDTO(MonitoringJob job) {
        return new MonitoringJobResponseDTO(
                job.getId(),
                job.getVideoId(),
                job.getMode(),
                job.getStatus(),
                job.getProgress(),
                job.getStartedAt(),
                job.getFinishedAt()
        );
    }
}
