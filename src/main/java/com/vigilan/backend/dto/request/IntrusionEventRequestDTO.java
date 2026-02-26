package com.vigilan.backend.dto.request;

import lombok.Data;

@Data
public class IntrusionEventRequestDTO {

    private Long monitoringJobId;
    private Long hazardZoneId;

    private Long objectId;

    private Double entryTimeSeconds;
    private Double exitTimeSeconds;
    private Double durationSeconds;

    private String screenshotUrl;
}