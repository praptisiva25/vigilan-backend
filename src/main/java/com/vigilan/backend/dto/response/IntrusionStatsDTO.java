package com.vigilan.backend.dto.response;

import java.time.LocalDate;

public class IntrusionStatsDTO {

    private LocalDate date;
    private Long count;

    public IntrusionStatsDTO(LocalDate date, Long count) {
        this.date = date;
        this.count = count;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getCount() {
        return count;
    }
}