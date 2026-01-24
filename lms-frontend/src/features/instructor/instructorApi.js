import api from "../../services/api";

/* COURSES */
export const getInstructorCoursesApi = () =>
  api.get("/instructor/courses");

export const createCourseApi = (data) =>
  api.post("/instructor/course", data);

/* VIDEOS */
// export const uploadVideoApi = (courseId, title, file) => {
//   const formData = new FormData();
//   formData.append("courseId", courseId);
//   formData.append("title", title);
//   formData.append("file", file);

//   return api.post("/instructor/video/upload", formData);
// };


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
  api.get(`/instructor/video/course/${courseId}`);

export const deleteVideoApi = (videoId) =>
  api.delete(`/instructor/video/${videoId}`);
