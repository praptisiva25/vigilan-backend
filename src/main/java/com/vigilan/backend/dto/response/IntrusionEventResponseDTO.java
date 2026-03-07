package com.vigilan.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IntrusionEventResponseDTO {

    private Long id;
    private Long jobId;
    private Long zoneId;

    private Long objectId;

    private Double entryTimeSeconds;
    private Double exitTimeSeconds;
    private Double durationSeconds;

    private String screenshotUrl;
    private String severity;
    private String blockedObjects;
    private String name;
}