package com.vigilan.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class MonitoringJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long videoId;

    private String mode; // LIVE or FAST

    private String status; // PENDING, RUNNING, COMPLETED

    private int progress;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;
}
