package com.vigilan.backend.service.impl;

import com.vigilan.backend.dto.response.VideoResponseDTO;
import com.vigilan.backend.entity.Video;
import com.vigilan.backend.repository.VideoRepository;
import com.vigilan.backend.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final S3Client s3Client;
    private final VideoRepository videoRepository;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Override
    public VideoResponseDTO uploadVideo(MultipartFile file,
                                        String cameraId,
                                        Double latitude, Double longitude) throws IOException {

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        String fileUrl = "https://" + bucketName + ".s3.amazonaws.com/" + fileName;

        Video video = new Video();
        video.setName(file.getOriginalFilename());
        video.setCameraId(cameraId);
        video.setLatitude(latitude);
        video.setLongitude(longitude);
        video.setFilePath(fileUrl);
        video.setUploadedAt(LocalDateTime.now());

        Video saved = videoRepository.save(video);

        return mapToDTO(saved);
    }

    @Override
    public List<VideoResponseDTO> getAllVideos() {
        return videoRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private VideoResponseDTO mapToDTO(Video video) {
        return new VideoResponseDTO(
                video.getId(),
                video.getName(),
                video.getCameraId(),
                video.getLatitude(),
                video.getLongitude(),
                video.getFilePath(),
                video.getUploadedAt()
        );
    }
}
