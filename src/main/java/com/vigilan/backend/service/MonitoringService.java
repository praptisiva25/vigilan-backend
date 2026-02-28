package com.vigilan.backend.service;

import com.vigilan.backend.dto.request.MonitoringJobUpdateRequestDTO;
import com.vigilan.backend.dto.response.MonitoringContextResponseDTO;
import com.vigilan.backend.dto.response.MonitoringJobResponseDTO;

public interface MonitoringService {

    MonitoringJobResponseDTO startMonitoring(Long videoId);

    MonitoringJobResponseDTO getStatus(Long jobId);

    MonitoringContextResponseDTO getMonitoringContext(Long jobId);

    void updateMonitoringJob(Long jobId, MonitoringJobUpdateRequestDTO request);
}