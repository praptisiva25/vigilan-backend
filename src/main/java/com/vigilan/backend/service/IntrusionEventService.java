package com.vigilan.backend.service;

import com.vigilan.backend.dto.request.IntrusionEventRequestDTO;
import com.vigilan.backend.dto.response.IntrusionEventResponseDTO;

import java.util.List;

public interface IntrusionEventService {

    void createIntrusion(IntrusionEventRequestDTO request);
    List<IntrusionEventResponseDTO> getByMonitoringJobId(Long jobId);
}