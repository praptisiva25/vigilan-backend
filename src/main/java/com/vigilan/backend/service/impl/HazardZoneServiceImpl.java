package com.vigilan.backend.service.impl;

import com.vigilan.backend.dto.request.HazardZoneRequestDTO;
import com.vigilan.backend.dto.response.HazardZoneResponseDTO;
import com.vigilan.backend.entity.HazardZone;
import com.vigilan.backend.entity.Video;
import com.vigilan.backend.repository.HazardZoneRepository;
import com.vigilan.backend.repository.VideoRepository;
import com.vigilan.backend.service.HazardZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HazardZoneServiceImpl implements HazardZoneService {

    private final HazardZoneRepository hazardZoneRepository;
    private final VideoRepository videoRepository;

    @Override
    public HazardZoneResponseDTO createZone(HazardZoneRequestDTO request) {

        Video video = videoRepository.findById(request.getVideoId())
                .orElseThrow(() -> new RuntimeException("Video not found"));

        HazardZone zone = new HazardZone();
        zone.setVideo(video);
        zone.setName(request.getName());
        zone.setSeverity(request.getSeverity());
        zone.setPolygonCoordinates(request.getPolygonCoordinates());
        zone.setAllowedObjects(request.getAllowedObjects());
        zone.setBlockedObjects(request.getBlockedObjects());

        HazardZone saved = hazardZoneRepository.save(zone);

        return mapToDTO(saved);
    }

    @Override
    public List<HazardZoneResponseDTO> getZonesByVideo(Long videoId) {

        return hazardZoneRepository.findByVideo_Id(videoId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private HazardZoneResponseDTO mapToDTO(HazardZone zone) {
        return new HazardZoneResponseDTO(
                zone.getId(),
                zone.getName(),
                zone.getSeverity(),
                zone.getPolygonCoordinates(),
                zone.getAllowedObjects(),
                zone.getBlockedObjects()
        );
    }
}