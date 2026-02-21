package com.vigilan.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HazardZoneResponseDTO {

    private Long id;
    private Long videoId;
    private String name;
    private String severity;
    private String polygonCoordinates;
    private String allowedObjects;
    private String blockedObjects;
}
