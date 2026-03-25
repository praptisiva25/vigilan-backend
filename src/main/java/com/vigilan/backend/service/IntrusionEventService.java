package com.vigilan.backend.service;

import com.vigilan.backend.dto.request.IntrusionEventRequestDTO;
import com.vigilan.backend.dto.response.CameraVideoStatsDTO;
import com.vigilan.backend.dto.response.IntrusionEventResponseDTO;
import com.vigilan.backend.dto.response.IntrusionStatsDTO;

import java.util.List;
import java.util.Map;

public interface IntrusionEventService {

    void createIntrusion(IntrusionEventRequestDTO request);
    List<IntrusionEventResponseDTO> getByMonitoringJobId(Long jobId);

    Map<String, Long> getIntrusionsPerCamera();

    Map<String, Object> getStatsByCamera(String cameraId);

    Map<String, Long> getSeverityByVideo(Long videoId);
}