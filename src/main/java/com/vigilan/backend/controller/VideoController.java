package com.vigilan.backend.controller;

import com.vigilan.backend.dto.response.VideoResponseDTO;
import com.vigilan.backend.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class VideoController {

    private final VideoService videoService;

    @PostMapping("/upload")
    public ResponseEntity<VideoResponseDTO> uploadVideo(
            @RequestParam MultipartFile file,
            @RequestParam String cameraId,
            @RequestParam Double latitude,
            @RequestParam Double longitude
    ) throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        VideoResponseDTO response =
                videoService.uploadVideo(file, cameraId, latitude, longitude);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<VideoResponseDTO> getAllVideos() {
        return videoService.getAllVideos();
    }
}
