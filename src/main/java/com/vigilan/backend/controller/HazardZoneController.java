package com.vigilan.backend.controller;

import com.vigilan.backend.dto.request.HazardZoneRequestDTO;
import com.vigilan.backend.dto.response.HazardZoneResponseDTO;
import com.vigilan.backend.service.HazardZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class HazardZoneController {

    private final HazardZoneService hazardZoneService;

    @PostMapping
    public HazardZoneResponseDTO createZone(
            @RequestBody HazardZoneRequestDTO request
    ) {
        return hazardZoneService.createZone(request);
    }

    @GetMapping("/video/{videoId}")
    public List<HazardZoneResponseDTO> getZonesByVideo(
            @PathVariable Long videoId
    ) {
        return hazardZoneService.getZonesByVideo(videoId);
    }
}
