package com.vigilan.backend.controller;

import com.vigilan.backend.dto.response.VideoResponseDTO;
import com.vigilan.backend.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PostMapping("/upload")
    public ResponseEntity<VideoResponseDTO> uploadVideo(
            @RequestParam MultipartFile file,
            @RequestParam String cameraId,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @AuthenticationPrincipal Jwt jwt
    ) throws IOException {

        String userId = jwt.getSubject();
        String email = jwt.getClaim("email");

        VideoResponseDTO response =
                videoService.uploadVideo(file, cameraId, latitude, longitude, userId, email);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<VideoResponseDTO>> getAllVideos(
            @RequestParam(required = false) String search,
            @AuthenticationPrincipal Jwt jwt
    ) {

        String userId = jwt.getSubject();

        return ResponseEntity.ok(videoService.getAllVideos(userId, search));
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity<Void> deleteVideo(
            @PathVariable Long videoId,
            @AuthenticationPrincipal Jwt jwt
    ) {

        String userId = jwt.getSubject();

        videoService.deleteVideo(videoId, userId);

        return ResponseEntity.noContent().build();
    }
}