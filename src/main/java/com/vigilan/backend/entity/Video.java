package com.vigilan.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String cameraId;

    private Double latitude;

    private Double longitude;

    private String filePath;

    private LocalDateTime uploadedAt;
}
