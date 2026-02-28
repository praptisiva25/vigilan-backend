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
    public MonitoringJobResponseDTO startMonitoring(Long videoId) {

        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found"));

        MonitoringJob job = MonitoringJob.builder()
                .video(video)
                .status("PENDING")
                .progress(0)
                .build();

        MonitoringJob saved = jobRepository.save(job);

        // Send message to SQS
        MonitoringJobMessage message = new MonitoringJobMessage(
                saved.getId(),
                saved.getVideo().getFilePath()
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
                        zone.getBlockedObjects()
                ))
                .toList();

        return new MonitoringContextResponseDTO(
                job.getId(),
                video.getFilePath(),
                zones
        );
    }

    @Override
    public void updateMonitoringJob(Long jobId, MonitoringJobUpdateRequestDTO request) {

        MonitoringJob job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // If already completed or failed, ignore updates
        if ("COMPLETED".equals(job.getStatus()) || "FAILED".equals(job.getStatus())) {
            return;
        }

        // Update status first
        if (request.getStatus() != null) {

            job.updateStatus(request.getStatus());

            if ("COMPLETED".equals(request.getStatus())) {
                job.updateProgress(100);
            }
        }

        // Update progress only if not completed
        if (request.getProgress() != null &&
                !"COMPLETED".equals(job.getStatus())) {

            int safeProgress = Math.min(request.getProgress(), 99);
            job.updateProgress(safeProgress);
        }

        jobRepository.save(job);
    }
}