package com.vigilan.backend.repository;

import com.vigilan.backend.entity.HazardZone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HazardZoneRepository extends JpaRepository<HazardZone, Long> {
    List<HazardZone> findByVideoId(Long videoId);
}
