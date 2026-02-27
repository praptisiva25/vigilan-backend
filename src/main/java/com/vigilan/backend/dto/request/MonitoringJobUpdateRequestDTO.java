package com.vigilan.backend.dto.request;

import lombok.Data;

@Data
public class MonitoringJobUpdateRequestDTO {

    private String status;
    private Integer progress;
}