package com.elearning.serviceImpl;

import com.elearning.entity.Course;
import com.elearning.entity.CourseVideo;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.CourseVideoRepository;
import com.elearning.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final CourseRepository courseRepository;
    private final CourseVideoRepository videoRepository;

    private static final String BASE_VIDEO_PATH =
            System.getProperty("user.home") + File.separator + "elearning-videos";

    @Override
    public void uploadVideo(Long courseId, MultipartFile file, String title) {

        // 🔴 ABSOLUTE PROOF CHECK
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Multipart file is EMPTY – controller config is wrong");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        try {
            File courseDir = new File(BASE_VIDEO_PATH, courseId.toString());
            if (!courseDir.exists() && !courseDir.mkdirs()) {
                throw new RuntimeException("Failed to create directory");
            }

            String originalName = file.getOriginalFilename();
            String safeName = System.currentTimeMillis() + "_" +
                    originalName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

            File outputFile = new File(courseDir, safeName);

            try (
                InputStream in = file.getInputStream();
                FileOutputStream out = new FileOutputStream(outputFile)
            ) {
                byte[] buffer = new byte[8192];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.flush();
            }

            if (!outputFile.exists() || outputFile.length() == 0) {
                throw new RuntimeException("File write failed");
            }

            CourseVideo video = CourseVideo.builder()
                    .course(course)
                    .title(title)
                    .filePath(outputFile.getAbsolutePath())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            videoRepository.save(video);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Video upload failed: " + e.getMessage());
        }
    }

    @Override
    public CourseVideo getVideo(Long videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found"));
    }

    @Override
    public List<CourseVideo> getVideosByCourse(Long courseId) {
        return videoRepository.findByCourse_CourseId(courseId);
    }
}
