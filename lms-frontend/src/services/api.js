// src/services/api.js
import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api",
});

// ✅ Attach JWT only
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    // 🔥 IMPORTANT:
    // DO NOT set Content-Type here
    // Axios will auto-set it:
    // - application/json for JSON
    // - multipart/form-data for FormData

    return config;
  },
  (error) => Promise.reject(error)
);

export default api;
