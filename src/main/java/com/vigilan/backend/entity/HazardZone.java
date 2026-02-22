package com.vigilan.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class HazardZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long videoId;

    private String name;

    private String severity;

    @Column(columnDefinition = "TEXT")
    private String polygonCoordinates;

    @Column(columnDefinition = "TEXT")
    private String allowedObjects;

    @Column(columnDefinition = "TEXT")
    private String blockedObjects;
}
