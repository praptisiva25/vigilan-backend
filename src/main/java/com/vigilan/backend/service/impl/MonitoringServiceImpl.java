package com.vigilan.backend.service.impl;

import com.vigilan.backend.dto.request.MonitoringJobUpdateRequestDTO;
import com.vigilan.backend.dto.response.HazardZoneResponseDTO;
import com.vigilan.backend.dto.response.MonitoringContextResponseDTO;
import com.vigilan.backend.dto.response.MonitoringJobResponseDTO;
import com.vigilan.backend.entity.MonitoringJob;
import com.vigilan.backend.entity.Video;
import com.vigilan.backend.repository.MonitoringJobRepository;
import com.vigilan.backend.repository.VideoRepository;
import com.vigilan.backend.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MonitoringServiceImpl implements MonitoringService {

    private final MonitoringJobRepository jobRepository;
    private final VideoRepository videoRepository;

    private static final String PYTHON_URL = "http://localhost:8000/process-job";

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

        // 🔥 CALL PYTHON SERVICE
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> payload = new HashMap<>();
        payload.put("jobId", saved.getId());
        payload.put("videoPath", saved.getVideo().getFilePath());
        payload.put("mode", saved.getMode());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(payload, headers);

        restTemplate.postForObject(PYTHON_URL, request, String.class);

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

        if (request.getStatus() != null) {
            job.setStatus(request.getStatus());
        }

        job.setProgress(request.getProgress());

        if ("COMPLETED".equals(request.getStatus())) {
            job.setFinishedAt(LocalDateTime.now());
        }

        jobRepository.save(job);
    }
}