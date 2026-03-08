package com.vigilan.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MonitoringJobResponseDTO {

    private Long jobId;
    private String status;
    private int progress;
    private LocalDateTime startedAt;
}