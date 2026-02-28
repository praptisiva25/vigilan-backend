package com.vigilan.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonitoringJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Integer progress;

    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    @PrePersist
    public void onCreate() {
        this.startedAt = LocalDateTime.now();

        if (this.progress == null) {
            this.progress = 0;
        }

        if (this.status == null) {
            this.status = "PENDING";
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Video video;


    @ManyToMany
    @JoinTable(
            name = "monitoring_job_zones",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "zone_id")
    )
    private List<HazardZone> hazardZones;


    @OneToMany(mappedBy = "monitoringJob", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IntrusionEvent> intrusionEvents;

    public void updateProgress(Integer progress) {
        this.progress = progress;
    }

    public void updateStatus(String status) {
        this.status = status;

        if ("COMPLETED".equals(status) || "FAILED".equals(status)) {
            this.finishedAt = LocalDateTime.now();
        }
    }
}