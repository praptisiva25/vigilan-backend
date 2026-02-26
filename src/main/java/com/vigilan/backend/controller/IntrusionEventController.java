package com.vigilan.backend.controller;

import com.vigilan.backend.dto.request.IntrusionEventRequestDTO;
import com.vigilan.backend.dto.response.IntrusionEventResponseDTO;
import com.vigilan.backend.service.IntrusionEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/intrusions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
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
}