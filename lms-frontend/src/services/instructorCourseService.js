import axios from "axios";

const API_URL = "http://localhost:8080/api/instructor";

/* ===== AUTH HEADER ===== */
const authHeader = () => ({
  headers: {
    Authorization: `Bearer ${localStorage.getItem("token")}`,
  },
});

/* ===== GET COURSES ===== */
const getMyCourses = () => {
  return axios.get(`${API_URL}/courses`, authHeader());
};

/* ===== DELETE COURSE (FIXED PATH) ===== */
const deleteCourse = (courseId) => {
  return axios.delete(`${API_URL}/course/${courseId}`, authHeader());
};

/* ===== UPDATE COURSE (EDIT) ===== */
const updateCourse = (courseId, data) => {
  return axios.put(`${API_URL}/course/${courseId}`, data, authHeader());
};

export default {
  getMyCourses,
  deleteCourse,
  updateCourse,
};
