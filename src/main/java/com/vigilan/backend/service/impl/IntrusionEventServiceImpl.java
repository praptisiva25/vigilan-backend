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

        MonitoringJob job = monitoringJobRepository.findById(request.getMonitoringJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        HazardZone zone = hazardZoneRepository.findById(request.getHazardZoneId())
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        IntrusionEvent event = new IntrusionEvent();
        event.setMonitoringJob(job);
        event.setHazardZone(zone);
        event.setObjectId(request.getObjectId());
        event.setEntryTimeSeconds(request.getEntryTimeSeconds());
        event.setExitTimeSeconds(request.getExitTimeSeconds());
        event.setDurationSeconds(request.getDurationSeconds());
        event.setScreenshotUrl(request.getScreenshotUrl());

        intrusionEventRepository.save(event);
    }

    private IntrusionEventResponseDTO mapToDTO(IntrusionEvent event) {
        return new IntrusionEventResponseDTO(
                event.getId(),
                event.getMonitoringJob().getId(),   // jobId
                event.getHazardZone().getId(),      // zoneId
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