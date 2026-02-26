package com.vigilan.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class MonitoringJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mode; // LIVE or FAST

    private String status; // PENDING, RUNNING, COMPLETED

    private int progress;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    @OneToMany(mappedBy = "monitoringJob", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IntrusionEvent> intrusionEvents;
}