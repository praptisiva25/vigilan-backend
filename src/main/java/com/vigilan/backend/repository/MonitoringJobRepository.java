package com.vigilan.backend.repository;

import com.vigilan.backend.entity.MonitoringJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitoringJobRepository extends JpaRepository<MonitoringJob, Long> {
}
