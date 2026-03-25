package com.vigilan.backend.controller;

import com.vigilan.backend.dto.request.IntrusionEventRequestDTO;
import com.vigilan.backend.dto.response.IntrusionEventResponseDTO;
import com.vigilan.backend.service.IntrusionEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/intrusions")
@RequiredArgsConstructor
public class IntrusionEventController {

    private final IntrusionEventService intrusionEventService;

    @PostMapping
    public void createIntrusion(@RequestBody IntrusionEventRequestDTO request) {
        intrusionEventService.createIntrusion(request);
    }

    @GetMapping("/job/{jobId}")
    public List<IntrusionEventResponseDTO> getByJob(@PathVariable Long jobId) {
        return intrusionEventService.getByMonitoringJobId(jobId);
    }

    @GetMapping("/stats/camera/{cameraId}")
    public Map<String, Object> getStatsByCamera(@PathVariable String cameraId) {
        return intrusionEventService.getStatsByCamera(cameraId);
    }

    @GetMapping("/stats/cameras")
    public Map<String, Long> getIntrusionsPerCamera() {
        return intrusionEventService.getIntrusionsPerCamera();
    }

    @GetMapping("/stats/video/{videoId}/severity")
    public Map<String, Long> getVideoSeverity(@PathVariable Long videoId) {
        return intrusionEventService.getSeverityByVideo(videoId);
    }
}