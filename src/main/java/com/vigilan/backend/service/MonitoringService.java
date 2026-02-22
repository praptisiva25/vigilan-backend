package com.vigilan.backend.service;

import com.vigilan.backend.dto.response.MonitoringJobResponseDTO;

public interface MonitoringService {

    MonitoringJobResponseDTO startMonitoring(Long videoId, String mode);

    MonitoringJobResponseDTO getStatus(Long jobId);
}