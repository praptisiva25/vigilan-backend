package com.vigilan.backend.service.impl;

import com.vigilan.backend.dto.queue.MonitoringJobMessage;
import com.vigilan.backend.dto.request.MonitoringJobUpdateRequestDTO;
import com.vigilan.backend.dto.response.HazardZoneResponseDTO;
import com.vigilan.backend.dto.response.MonitoringContextResponseDTO;
import com.vigilan.backend.dto.response.MonitoringJobResponseDTO;
import com.vigilan.backend.entity.MonitoringJob;
import com.vigilan.backend.entity.Video;
import com.vigilan.backend.repository.MonitoringJobRepository;
import com.vigilan.backend.repository.VideoRepository;
import com.vigilan.backend.service.MonitoringService;
import com.vigilan.backend.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonitoringServiceImpl implements MonitoringService {

    private final MonitoringJobRepository jobRepository;
    private final VideoRepository videoRepository;
    private final QueueService queueService;

    @Override
    public MonitoringJobResponseDTO startMonitoring(Long videoId, String mode) {

        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found"));

        MonitoringJob job = new MonitoringJob();
        job.setVideo(video);
        job.setMode(mode);
        job.setStatus("PENDING");
        job.setProgress(0);
        job.setStartedAt(LocalDateTime.now());

        MonitoringJob saved = jobRepository.save(job);

        // Send message to SQS
        MonitoringJobMessage message = new MonitoringJobMessage(
                saved.getId(),
                saved.getVideo().getFilePath(),
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
                .orElseThrow(() -> new RuntimeException("Job not found"));

        return new MonitoringJobResponseDTO(
                job.getId(),
                job.getStatus(),
                job.getProgress()
        );
    }

    @Override
    public MonitoringContextResponseDTO getMonitoringContext(Long jobId) {

        MonitoringJob job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Video video = job.getVideo();

        List<HazardZoneResponseDTO> zones = video.getHazardZones()
                .stream()
                .map(zone -> new HazardZoneResponseDTO(
                        zone.getId(),
                        zone.getName(),
                        zone.getSeverity(),
                        zone.getPolygonCoordinates(),
                        zone.getAllowedObjects(),
                        zone.getBlockedObjects()
                ))
                .toList();

        return new MonitoringContextResponseDTO(
                job.getId(),
                job.getMode(),
                video.getFilePath(),
                zones
        );
    }

    @Override
    public void updateMonitoringJob(Long jobId, MonitoringJobUpdateRequestDTO request) {

        MonitoringJob job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // 🔒 If already completed or failed, ignore further updates
        if ("COMPLETED".equals(job.getStatus()) || "FAILED".equals(job.getStatus())) {
            return;
        }

        // Update status first
        if (request.getStatus() != null) {
            job.setStatus(request.getStatus());

            if ("COMPLETED".equals(request.getStatus())) {
                job.setProgress(100);  // Force 100%
                job.setFinishedAt(LocalDateTime.now());
            }

            if ("FAILED".equals(request.getStatus())) {
                job.setFinishedAt(LocalDateTime.now());
            }
        }


        if (request.getProgress() != null && !"COMPLETED".equals(job.getStatus())) {

            int safeProgress = Math.min(request.getProgress(), 99);
            job.setProgress(safeProgress);
        }

        jobRepository.save(job);
    }
}