package com.vigilan.backend.service.impl;

import com.vigilan.backend.dto.request.IntrusionEventRequestDTO;
import com.vigilan.backend.dto.response.IntrusionEventResponseDTO;
import com.vigilan.backend.entity.HazardZone;
import com.vigilan.backend.entity.IntrusionEvent;
import com.vigilan.backend.entity.MonitoringJob;
import com.vigilan.backend.repository.HazardZoneRepository;
import com.vigilan.backend.repository.IntrusionEventRepository;
import com.vigilan.backend.repository.MonitoringJobRepository;
import com.vigilan.backend.service.IntrusionEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IntrusionEventServiceImpl implements IntrusionEventService {

    private final IntrusionEventRepository intrusionEventRepository;
    private final MonitoringJobRepository monitoringJobRepository;
    private final HazardZoneRepository hazardZoneRepository;

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

        if (request.getDurationSeconds() < 0.5) {
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

        intrusionEventRepository.save(event);
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
                event.getScreenshotUrl()
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
}