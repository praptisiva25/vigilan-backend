package com.vigilan.backend.service;

import com.vigilan.backend.dto.request.HazardZoneRequestDTO;
import com.vigilan.backend.dto.response.HazardZoneResponseDTO;

import java.util.List;

public interface HazardZoneService {

    HazardZoneResponseDTO createZone(HazardZoneRequestDTO request);

    List<HazardZoneResponseDTO> getZonesByVideo(Long videoId);
}
