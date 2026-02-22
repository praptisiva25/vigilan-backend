package com.vigilan.backend.controller;

import com.vigilan.backend.dto.request.HazardZoneRequestDTO;
import com.vigilan.backend.dto.response.HazardZoneResponseDTO;
import com.vigilan.backend.service.HazardZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
public class HazardZoneController {

    private final HazardZoneService hazardZoneService;

    @PostMapping
    public ResponseEntity<HazardZoneResponseDTO> createZone(
            @RequestBody HazardZoneRequestDTO request
    ) {
        return ResponseEntity.ok(hazardZoneService.createZone(request));
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<HazardZoneResponseDTO>> getZonesByVideo(
            @PathVariable Long videoId
    ) {
        return ResponseEntity.ok(
                hazardZoneService.getZonesByVideo(videoId)
        );
    }
}