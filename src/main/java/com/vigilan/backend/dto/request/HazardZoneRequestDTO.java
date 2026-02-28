package com.vigilan.backend.dto.request;

import lombok.Data;

@Data
public class HazardZoneRequestDTO {

    private Long videoId;
    private String name;
    private String severity;
    private String polygonCoordinates;
    private String blockedObjects;
}
