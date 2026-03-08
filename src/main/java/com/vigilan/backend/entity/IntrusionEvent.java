package com.vigilan.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntrusionEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long objectId;

    @Column(nullable = false)
    private Double entryTimeSeconds;

    private Double exitTimeSeconds;
    private Double durationSeconds;

    @Column(columnDefinition = "TEXT")
    private String screenshotUrl;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monitoring_job_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MonitoringJob monitoringJob;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hazard_zone_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private HazardZone hazardZone;


    public void markExit(Double exitTimeSeconds) {
        this.exitTimeSeconds = exitTimeSeconds;
        if (exitTimeSeconds != null && entryTimeSeconds != null) {
            this.durationSeconds = exitTimeSeconds - entryTimeSeconds;
        }
    }
}