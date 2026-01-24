import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { toast } from "react-toastify";

import {
  getVideosByCourseApi,
  uploadVideoApi,
  deleteVideoApi,
} from "../../features/instructor/instructorApi";

import "./InstructorCourses.css";

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
    } catch (err) {
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
      toast.error("Title and file required");
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
    } catch (err) {
      console.error(err);
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
    <div className="instructor-courses-page">
      <h1 className="page-title">🎬 Manage Videos</h1>

      {/* ================= UPLOAD FORM ================= */}
      <form onSubmit={uploadVideo} className="glass p-3 mb-4">
        <input
          className="form-control mb-2"
          placeholder="Video title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />

        <input
          id="videoFile"
          type="file"
          className="form-control mb-2"
          accept="video/*"
          onChange={(e) => setFile(e.target.files[0])}
        />

        <button
          className="btn btn-gradient w-100"
          disabled={loading}
        >
          {loading ? "Uploading..." : "Upload Video"}
        </button>
      </form>

      {/* ================= VIDEO LIST ================= */}
      {videos.length === 0 ? (
        <p style={{ color: "white" }}>No videos uploaded</p>
      ) : (
        <table className="video-table">
          <thead>
            <tr>
              <th>Title</th>
              <th>File</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {videos.map((v) => (
              <tr key={v.videoId}>
                <td>{v.title}</td>
                <td className="truncate">{v.filePath}</td>
                <td>
                  <button
                    className="delete"
                    onClick={() => deleteVideo(v.videoId)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default InstructorCourseVideos;
