import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import {
  getCourseVideosApi,
  streamVideoApi,
} from "../../services/studentApi";

const StudentCourseVideos = () => {
  const { courseId } = useParams();
  const navigate = useNavigate();

  const [videos, setVideos] = useState([]);
  const [videoUrl, setVideoUrl] = useState(null);
  const [activeTitle, setActiveTitle] = useState("");

  useEffect(() => {
    loadVideos();
  }, []);

  const loadVideos = async () => {
    try {
      const res = await getCourseVideosApi(courseId);
      setVideos(res.data);
    } catch {
      toast.error("Failed to load videos");
    }
  };

  const playVideo = async (video) => {
    try {
      const blob = await streamVideoApi(video.videoId);
      const url = URL.createObjectURL(blob);

      setVideoUrl(url);
      setActiveTitle(video.title);
    } catch (err) {
      toast.error(
        err.message === "Video stream failed"
          ? "Video file missing on server"
          : "Unauthorized"
      );
    }
  };

  return (
    <div className="instructor-courses-page">
      {/* HEADER */}
      <div className="dashboard-header">
        <h1 className="page-title">🎬 Course Videos</h1>
        <button
          className="btn btn-outline-light"
          onClick={() => navigate("/student/my-courses")}
        >
          ⬅ Back
        </button>
      </div>

      {/* PLAYER */}
      {videoUrl && (
        <div className="course-card">
          <h3>{activeTitle}</h3>
          <video
            src={videoUrl}
            controls
            controlsList="nodownload"
            style={{ width: "100%", borderRadius: "10px" }}
          />
        </div>
      )}

      {/* VIDEO LIST */}
      <div className="course-grid">
        {videos.map((v) => (
          <div className="course-card" key={v.videoId}>
            <h3>{v.title}</h3>

            <button
              className="video"
              onClick={() => playVideo(v)}
            >
              ▶ Play Video
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default StudentCourseVideos;
