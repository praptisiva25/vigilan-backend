package com.vigilan.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class HazardZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String severity;

    @Column(columnDefinition = "TEXT")
    private String polygonCoordinates;

    @Column(columnDefinition = "TEXT")
    private String allowedObjects;

    @Column(columnDefinition = "TEXT")
    private String blockedObjects;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;
}