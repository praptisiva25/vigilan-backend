package com.vigilan.backend.service;

import com.vigilan.backend.dto.response.MonitoringJobResponseDTO;
import com.vigilan.backend.entity.MonitoringJob;

public interface MonitoringService {

    MonitoringJobResponseDTO startMonitoring(Long videoId, String mode);

    MonitoringJobResponseDTO getStatus(Long jobId);


    void processVideo(MonitoringJob job);
}
