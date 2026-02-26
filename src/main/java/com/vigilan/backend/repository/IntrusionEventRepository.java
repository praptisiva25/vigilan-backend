package com.vigilan.backend.repository;

import com.vigilan.backend.entity.IntrusionEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IntrusionEventRepository extends JpaRepository<IntrusionEvent, Long> {

    List<IntrusionEvent> findByMonitoringJob_Id(Long jobId);

}