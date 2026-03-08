package com.vigilan.backend.service.impl;

import com.vigilan.backend.dto.request.IntrusionEventRequestDTO;
import com.vigilan.backend.dto.response.IntrusionEventResponseDTO;
import com.vigilan.backend.dto.response.IntrusionStatsDTO;
import com.vigilan.backend.entity.HazardZone;
import com.vigilan.backend.entity.IntrusionEvent;
import com.vigilan.backend.entity.MonitoringJob;
import com.vigilan.backend.repository.HazardZoneRepository;
import com.vigilan.backend.repository.IntrusionEventRepository;
import com.vigilan.backend.repository.MonitoringJobRepository;
import com.vigilan.backend.service.IntrusionEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IntrusionEventServiceImpl implements IntrusionEventService {

    private final IntrusionEventRepository intrusionEventRepository;
    private final MonitoringJobRepository monitoringJobRepository;
    private final HazardZoneRepository hazardZoneRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void createIntrusion(IntrusionEventRequestDTO request) {

        if (request.getMonitoringJobId() == null ||
                request.getHazardZoneId() == null ||
                request.getObjectId() == null ||
                request.getEntryTimeSeconds() == null ||
                request.getExitTimeSeconds() == null ||
                request.getDurationSeconds() == null) {
            return;
        }

        if (request.getDurationSeconds() < 0.7) {
            return;
        }

        boolean exists = intrusionEventRepository
                .existsByMonitoringJob_IdAndObjectIdAndEntryTimeSeconds(
                        request.getMonitoringJobId(),
                        request.getObjectId(),
                        request.getEntryTimeSeconds()
                );

        if (exists) {
            return;
        }

        MonitoringJob job = monitoringJobRepository.findById(request.getMonitoringJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        HazardZone zone = hazardZoneRepository.findById(request.getHazardZoneId())
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        IntrusionEvent event = IntrusionEvent.builder()
                .monitoringJob(job)
                .hazardZone(zone)
                .objectId(request.getObjectId())
                .entryTimeSeconds(request.getEntryTimeSeconds())
                .exitTimeSeconds(request.getExitTimeSeconds())
                .durationSeconds(request.getDurationSeconds())
                .screenshotUrl(request.getScreenshotUrl())
                .build();

        IntrusionEvent saved = intrusionEventRepository.save(event);

        IntrusionEventResponseDTO dto = mapToDTO(saved);

        messagingTemplate.convertAndSend(
                "/topic/intrusions/" + job.getId(),
                dto
        );
    }

    private IntrusionEventResponseDTO mapToDTO(IntrusionEvent event) {
        return new IntrusionEventResponseDTO(
                event.getId(),
                event.getMonitoringJob().getId(),
                event.getHazardZone().getId(),
                event.getObjectId(),
                event.getEntryTimeSeconds(),
                event.getExitTimeSeconds(),
                event.getDurationSeconds(),
                event.getScreenshotUrl(),
                event.getHazardZone().getSeverity(),
                event.getHazardZone().getBlockedObjects(),
                event.getHazardZone().getName(),
                event.getMonitoringJob().getStartedAt()

        );
    }

    @Override
    public List<IntrusionEventResponseDTO> getByMonitoringJobId(Long jobId) {
        return intrusionEventRepository
                .findByMonitoringJob_Id(jobId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public Map<String, Long> getIntrusionsPerCamera() {

        List<IntrusionEvent> events = intrusionEventRepository.findAll();

        return events.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getMonitoringJob().getVideo().getName(),
                        Collectors.counting()
                ));
    }

    @Override
    public List<IntrusionStatsDTO> getIntrusionStatsByVideo(Long videoId) {

        List<IntrusionEvent> events =
                intrusionEventRepository.findByMonitoringJob_Video_Id(videoId);

        Map<LocalDate, Long> stats = events.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getMonitoringJob().getStartedAt().toLocalDate(),
                        Collectors.counting()
                ));

        return stats.entrySet().stream()
                .map(e -> new IntrusionStatsDTO(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(IntrusionStatsDTO::getDate))
                .toList();
    }
}