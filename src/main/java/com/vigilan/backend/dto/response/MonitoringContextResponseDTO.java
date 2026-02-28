package com.vigilan.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class MonitoringContextResponseDTO {

    private Long jobId;
    private String videoPath;
    private List<HazardZoneResponseDTO> hazardZones;
}