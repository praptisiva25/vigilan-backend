package com.vigilan.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MonitoringJobResponseDTO {

    private Long id;
    private Long videoId;
    private String mode;
    private String status;
    private int progress;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
}
