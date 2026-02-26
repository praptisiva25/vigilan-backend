package com.vigilan.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class IntrusionEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long objectId;

    private Double entryTimeSeconds;

    private Double exitTimeSeconds;

    private Double durationSeconds;

    private String screenshotUrl;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monitoring_job_id", nullable = false)
    private MonitoringJob monitoringJob;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hazard_zone_id", nullable = false)
    private HazardZone hazardZone;
}