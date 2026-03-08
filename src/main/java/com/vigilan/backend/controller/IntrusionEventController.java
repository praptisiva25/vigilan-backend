package com.vigilan.backend.controller;

import com.vigilan.backend.dto.request.IntrusionEventRequestDTO;
import com.vigilan.backend.dto.response.IntrusionEventResponseDTO;
import com.vigilan.backend.dto.response.IntrusionStatsDTO;
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

    @GetMapping("/stats/video/{videoId}")
    public List<IntrusionStatsDTO> getStats(@PathVariable Long videoId) {
        return intrusionEventService.getIntrusionStatsByVideo(videoId);
    }

    @GetMapping("/stats/cameras")
    public Map<String, Long> getIntrusionsPerCamera() {
        return intrusionEventService.getIntrusionsPerCamera();
    }
}