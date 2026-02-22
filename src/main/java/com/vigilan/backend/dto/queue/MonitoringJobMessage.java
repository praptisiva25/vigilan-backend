package com.vigilan.backend.dto.queue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonitoringJobMessage {

    private Long jobId;
    private Long videoId;
    private String mode;
}