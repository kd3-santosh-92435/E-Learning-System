import api from "../../services/api";

// ================= STUDENT =================

export const studentRegisterApi = (data) => {
  return api.post("/auth/student/register", data); // ✅
};

export const studentLoginApi = (data) => {
  return api.post("/auth/student/login", data); // ✅
};

// ================= INSTRUCTOR =================

export const instructorRegisterApi = (data) => {
  return api.post("/auth/instructor/register", data);
};

export const instructorLoginApi = (data) => {
  return api.post("/auth/instructor/login", data);
};

// ================= ADMIN =================

export const adminLoginApi = (data) => {
  return api.post("/auth/admin/login", data);
};
