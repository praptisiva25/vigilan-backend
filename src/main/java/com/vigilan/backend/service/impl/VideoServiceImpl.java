package com.vigilan.backend.service.impl;

import com.vigilan.backend.dto.response.VideoResponseDTO;
import com.vigilan.backend.entity.MonitoringJob;
import com.vigilan.backend.entity.User;
import com.vigilan.backend.entity.Video;
import com.vigilan.backend.repository.UserRepository;
import com.vigilan.backend.repository.VideoRepository;
import com.vigilan.backend.service.VideoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final S3Client s3Client;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    @Value("${AWS_S3_BUCKET}")
    private String bucketName;

    @Override
    public VideoResponseDTO uploadVideo(MultipartFile file,
                                        String cameraId,
                                        String description,
                                        Double latitude,
                                        Double longitude,
                                        String userId,
                                        String email) throws IOException {

        // Ensure user exists
        User user = userRepository.findById(userId)
                .orElseGet(() ->
                        userRepository.save(
                                User.builder()
                                        .id(userId)
                                        .email(email)
                                        .build()
                        )
                );

        // Upload to S3
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        String fileUrl = "https://" + bucketName + ".s3.amazonaws.com/" + fileName;

        // Build Video entity
        Video video = Video.builder()
                .name(file.getOriginalFilename())
                .cameraId(cameraId)
                .latitude(latitude)
                .longitude(longitude)
                .filePath(fileUrl)
                .user(user)
                .build();

        Video saved = videoRepository.save(video);

        return mapToDTO(saved);
    }

    private VideoResponseDTO mapToDTO(Video video) {
        return new VideoResponseDTO(
                video.getId(),
                video.getName(),
                video.getCameraId(),
                video.getDescription(),
                video.getLatitude(),
                video.getLongitude(),
                video.getFilePath(),
                video.getUploadedAt()
        );
    }

    @Override
    public List<VideoResponseDTO> getAllVideos(String userId,String search) {

        List<Video> videos;

        if (search != null && !search.isBlank()) {
            videos = videoRepository
                    .findByUserIdAndNameContainingIgnoreCase(userId, search);
        } else {
            videos = videoRepository.findByUserId(userId);
        }

        return videos.stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Transactional
    @Override
    public void deleteVideo(Long videoId, String userId) {

        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found"));

        if (!video.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        for (MonitoringJob job : video.getMonitoringJobs()) {
            job.getHazardZones().clear();
        }

        String fileUrl = video.getFilePath();
        String key = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());

        videoRepository.delete(video);
    }


}