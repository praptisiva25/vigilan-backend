package com.vigilan.backend.service;

import com.vigilan.backend.dto.response.VideoResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface VideoService {

    VideoResponseDTO uploadVideo(MultipartFile file,
                                 String cameraId,
                                 Double latitude,
                                 Double longitude,
                                 String userId,
                                 String email) throws IOException;

    List<VideoResponseDTO> getAllVideos(String userId);

    void deleteVideo(Long videoId, String userId);
}
