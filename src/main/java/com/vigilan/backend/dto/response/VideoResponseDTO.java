package com.vigilan.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class VideoResponseDTO {

    private Long id;
    private String name;
    private String cameraId;
    private String description;
    private Double latitude;
    private Double longitude;
    private String filePath;
    private LocalDateTime uploadedAt;
}
