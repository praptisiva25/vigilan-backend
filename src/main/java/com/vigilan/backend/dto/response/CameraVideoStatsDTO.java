package com.vigilan.backend.dto.response;

import java.time.LocalDate;

public class CameraVideoStatsDTO {

    private String videoName;
    private LocalDate date;
    private Long count;

    public CameraVideoStatsDTO(String videoName, LocalDate date, Long count) {
        this.videoName = videoName;
        this.date = date;
        this.count = count;
    }

    public String getVideoName() { return videoName; }
    public LocalDate getDate() { return date; }
    public Long getCount() { return count; }
}