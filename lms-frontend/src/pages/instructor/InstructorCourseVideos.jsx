import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { toast } from "react-toastify";

import {
  getVideosByCourseApi,
  uploadVideoApi,
  deleteVideoApi,
} from "../../features/instructor/instructorApi";

import "./InstructorCourseVideos.css";

const InstructorCourseVideos = () => {
  const { courseId } = useParams();

  const [videos, setVideos] = useState([]);
  const [title, setTitle] = useState("");
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);

  // ================= LOAD VIDEOS =================
  const loadVideos = async () => {
    try {
      const res = await getVideosByCourseApi(courseId);
      setVideos(res.data);
    } catch {
      toast.error("Failed to load videos");
    }
  };

  useEffect(() => {
    loadVideos();
  }, [courseId]);

  // ================= UPLOAD =================
  const uploadVideo = async (e) => {
    e.preventDefault();

    if (!title || !file) {
      toast.error("Title and video file required");
      return;
    }

    try {
      setLoading(true);
      await uploadVideoApi(courseId, title, file);
      toast.success("Video uploaded successfully");

      setTitle("");
      setFile(null);
      document.getElementById("videoFile").value = "";

      loadVideos();
    } catch {
      toast.error("Upload failed");
    } finally {
      setLoading(false);
    }
  };

  // ================= DELETE =================
  const deleteVideo = async (videoId) => {
    if (!window.confirm("Delete this video?")) return;

    try {
      await deleteVideoApi(videoId);
      toast.success("Video deleted");
      loadVideos();
    } catch {
      toast.error("Delete failed");
    }
  };

  return (
    <div className="instructor-video-page">
      <h1 className="page-title">🎬 Course Content</h1>

      {/* ================= UPLOAD CARD ================= */}
      <div className="upload-card">
        <h3>Upload New Video</h3>

        <form onSubmit={uploadVideo}>
          <input
            type="text"
            placeholder="Lecture title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />

          <input
            id="videoFile"
            type="file"
            accept="video/*"
            onChange={(e) => setFile(e.target.files[0])}
          />

          <button disabled={loading}>
            {loading ? "Uploading..." : "Upload Lecture"}
          </button>
        </form>
      </div>

      {/* ================= VIDEO LIST ================= */}
      <h2 className="section-title">Course Lectures</h2>

      {videos.length === 0 ? (
        <p className="empty-text">No videos uploaded yet</p>
      ) : (
        <div className="video-grid">
          {videos.map((v, index) => (
            <div className="video-card" key={v.videoId}>
              <div className="video-thumb">
                🎥
              </div>

              <div className="video-info">
                <h4>
                  {index + 1}. {v.title}
                </h4>

                <p className="file-path">
                  {v.filePath.split("\\").pop()}
                </p>

                <div className="actions">
                  <button
                    className="delete-btn"
                    onClick={() => deleteVideo(v.videoId)}
                  >
                    Delete
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default InstructorCourseVideos;
