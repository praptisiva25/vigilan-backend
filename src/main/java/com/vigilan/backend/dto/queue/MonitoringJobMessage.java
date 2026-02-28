package com.vigilan.backend.dto.queue;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonitoringJobMessage {

    private Long jobId;
    private String videoPath;
}