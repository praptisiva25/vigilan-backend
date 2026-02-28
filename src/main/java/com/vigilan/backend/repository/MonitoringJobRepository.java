package com.vigilan.backend.repository;

import com.vigilan.backend.entity.MonitoringJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonitoringJobRepository extends JpaRepository<MonitoringJob, Long> {
    List<MonitoringJob> findByVideo_IdOrderByIdDesc(Long videoId);
}
