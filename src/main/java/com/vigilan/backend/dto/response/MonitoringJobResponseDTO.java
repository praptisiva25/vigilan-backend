package com.vigilan.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonitoringJobResponseDTO {

    private Long jobId;
    private String status;
    private int progress;
}