


// src/features/instructor/instructorApi.js
import api from "../../services/api";

/* VIDEOS */
export const uploadVideoApi = (courseId, title, file) => {
  const formData = new FormData();
  formData.append("title", title);
  formData.append("file", file);

  return api.post(
    `/instructor/course/${courseId}/video`,
    formData
  );
};

export const getVideosByCourseApi = (courseId) =>
  api.get(`/instructor/course/${courseId}/videos`);

export const deleteVideoApi = (videoId) =>
  api.delete(`/instructor/video/${videoId}`);

/* COURSES */
export const getInstructorCoursesApi = () =>
  api.get("/instructor/courses");

export const createCourseApi = (data) =>
  api.post("/instructor/course", data);