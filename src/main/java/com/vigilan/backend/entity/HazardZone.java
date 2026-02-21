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

    @Lob
    private String polygonCoordinates; // JSON string

    @Lob
    private String allowedObjects; // JSON string

    @Lob
    private String blockedObjects; // JSON string
}
