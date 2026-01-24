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

/* ✅ COURSE VIDEOS (MATCHES BACKEND) */
export const getCourseVideosApi = (courseId) =>
  api.get(`/student/videos/course/${courseId}`);





/* PAYMENT */
export const createOrderApi = (courseId) =>
  api.post(`/student/payment/create-order/${courseId}`);

export const verifyPaymentApi = (data) =>
  api.post("/student/payment/verify", null, {
    params: data,
  });


 export const streamVideoApi = async (videoId) => {
  const token = localStorage.getItem("token");

  const res = await fetch(
    `http://localhost:8080/api/student/videos/${videoId}`,
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );

  if (!res.ok) {
    throw new Error("Video stream failed");
  }

  return await res.blob();
};