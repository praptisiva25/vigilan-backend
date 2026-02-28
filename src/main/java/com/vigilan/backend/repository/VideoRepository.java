package com.vigilan.backend.repository;

import com.vigilan.backend.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByUserId(String userId);

    List<Video> findByUserIdAndNameContainingIgnoreCase(String userId, String search);
}
