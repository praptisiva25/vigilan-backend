package com.vigilan.backend.service;

import com.vigilan.backend.dto.request.MonitoringJobUpdateRequestDTO;
import com.vigilan.backend.dto.response.MonitoringContextResponseDTO;
import com.vigilan.backend.dto.response.MonitoringJobResponseDTO;

import java.util.List;

public interface MonitoringService {

    MonitoringJobResponseDTO startMonitoring(Long videoId, List<Long> zoneIds);

    MonitoringJobResponseDTO getStatus(Long jobId);

    MonitoringContextResponseDTO getMonitoringContext(Long jobId);

    void updateMonitoringJob(Long jobId, MonitoringJobUpdateRequestDTO request);

    List<MonitoringJobResponseDTO> getJobsByVideo(Long videoId);
}