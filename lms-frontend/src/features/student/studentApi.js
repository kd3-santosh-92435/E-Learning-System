import api from "./api";

/* AVAILABLE COURSES */
export const getAllCoursesApi = () =>
  api.get("/student/courses");

/* ENROLL */
export const enrollCourseApi = (courseId) =>
  api.post(`/student/enroll/${courseId}`);

/* MY COURSES */
export const getMyCoursesApi = () =>
  api.get("/student/my-courses");

/* 🔥 COURSE VIDEOS */

export const getCourseVideosApi = (courseId) =>
  api.get(`/student/video/course/${courseId}`);


