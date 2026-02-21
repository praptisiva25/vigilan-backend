package com.vigilan.backend.service;

import com.vigilan.backend.dto.response.VideoResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface VideoService {

    VideoResponseDTO uploadVideo(MultipartFile file, String cameraId, Double latitude, Double longitude) throws IOException;;

    List<VideoResponseDTO> getAllVideos();

}
