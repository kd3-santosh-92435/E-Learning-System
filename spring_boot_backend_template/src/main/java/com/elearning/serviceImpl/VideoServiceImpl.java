package com.elearning.serviceImpl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.elearning.dtos.VideoResponseDTO;
import com.elearning.entity.Course;
import com.elearning.entity.Video;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.VideoRepository;
import com.elearning.service.VideoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class VideoServiceImpl implements VideoService {

    private final CourseRepository courseRepository;
    private final VideoRepository videoRepository;

    @Value("${video.upload.dir}")
    private String uploadDir;

    // ================= UPLOAD =================
    @Override
    public VideoResponseDTO uploadVideo(
            Long courseId,
            String title,
            MultipartFile file
    ) {
        try {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Course not found"));

            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path targetPath = uploadPath.resolve(fileName);

            Files.copy(
                file.getInputStream(),
                targetPath,
                StandardCopyOption.REPLACE_EXISTING
            );

            Video video = Video.builder()
                    .title(title)
                    .filePath(targetPath.toString()) // absolute path
                    .duration(0)
                    .course(course)
                    .build();


            Video saved = videoRepository.save(video);

            return new VideoResponseDTO(
                    saved.getVideoId(),
                    saved.getTitle(),
                    saved.getFilePath()
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Video upload failed"
            );
        }
    }

    // ================= LIST =================
    @Override
    public List<VideoResponseDTO> getVideosByCourse(Long courseId) {
        return videoRepository.findByCourse_CourseId(courseId)
                .stream()
                .map(v -> new VideoResponseDTO(
                        v.getVideoId(),
                        v.getTitle(),
                        v.getFilePath()
                ))
                .collect(Collectors.toList());
    }

    // ================= GET ONE =================
    @Override
    public Video getVideo(Long videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Video not found"
                        ));
    }

    // ================= DELETE =================
    @Override
    public void deleteVideo(Long videoId) {

        Video video = videoRepository.findById(videoId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Video not found"
                        ));

        try {
            Files.deleteIfExists(
                    Paths.get(video.getFilePath()) // ✅ DIRECT PATH
            );
        } catch (Exception ignored) {}

        videoRepository.delete(video);
    }
}
