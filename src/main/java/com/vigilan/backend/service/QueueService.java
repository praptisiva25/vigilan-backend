package com.vigilan.backend.service;

import com.vigilan.backend.dto.queue.MonitoringJobMessage;

public interface QueueService {

    void sendMonitoringJob(MonitoringJobMessage message);
}