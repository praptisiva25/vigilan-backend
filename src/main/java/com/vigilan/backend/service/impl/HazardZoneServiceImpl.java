package com.vigilan.backend.service.impl;

import com.vigilan.backend.dto.request.HazardZoneRequestDTO;
import com.vigilan.backend.dto.response.HazardZoneResponseDTO;
import com.vigilan.backend.entity.HazardZone;
import com.vigilan.backend.repository.HazardZoneRepository;
import com.vigilan.backend.service.HazardZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HazardZoneServiceImpl implements HazardZoneService {

    private final HazardZoneRepository hazardZoneRepository;

    @Override
    public HazardZoneResponseDTO createZone(HazardZoneRequestDTO request) {

        HazardZone zone = new HazardZone();
        zone.setVideoId(request.getVideoId());
        zone.setName(request.getName());
        zone.setSeverity(request.getSeverity());
        zone.setPolygonCoordinates(request.getPolygonCoordinates());

        HazardZone saved = hazardZoneRepository.save(zone);

        return mapToDTO(saved);
    }

    @Override
    public List<HazardZoneResponseDTO> getZonesByVideo(Long videoId) {

        return hazardZoneRepository.findByVideoId(videoId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private HazardZoneResponseDTO mapToDTO(HazardZone zone) {
        return new HazardZoneResponseDTO(
                zone.getId(),
                zone.getVideoId(),
                zone.getName(),
                zone.getSeverity(),
                zone.getPolygonCoordinates(),
                zone.getAllowedObjects(),
                zone.getBlockedObjects()
        );
    }
}