import React from "react";
import { Routes, Route } from "react-router-dom";

/* ========= PUBLIC ========= */
import LandingPage from "./pages/public/LandingPage";
import About from "./pages/public/About";
import Contact from "./pages/public/Contact";

/* ========= AUTH ========= */
import StudentLogin from "./pages/auth/StudentLogin";
import StudentRegister from "./pages/auth/StudentRegister";
import InstructorLogin from "./pages/auth/InstructorLogin";
import InstructorRegister from "./pages/auth/InstructorRegister";
import AdminLogin from "./pages/auth/AdminLogin";

/* ========= STUDENT ========= */
import StudentDashboard from "./pages/student/StudentDashboard";
import StudentMyCourses from "./pages/student/StudentMyCourses";
import StudentCourseVideos from "./pages/student/StudentCourseVideos";

/* ========= INSTRUCTOR ========= */
import InstructorDashboard from "./pages/instructor/InstructorDashboard";
import InstructorCourses from "./pages/instructor/InstructorCourses";
import InstructorCourseVideos from "./pages/instructor/InstructorCourseVideos";
import InstructorLayout from "./layouts/InstructorLayout";

/* ========= ADMIN ========= */
import AdminDashboard from "./pages/admin/AdminDashboard";

/* ========= PROTECTION ========= */
import ProtectedRoute from "./routes/ProtectedRoute";

function App() {
  return (
    <Routes>

      {/* ---------- PUBLIC ---------- */}
      <Route path="/" element={<LandingPage />} />
      <Route path="/about" element={<About />} />
      <Route path="/contact" element={<Contact />} />

      {/* ---------- AUTH ---------- */}
      <Route path="/student/login" element={<StudentLogin />} />
      <Route path="/student/register" element={<StudentRegister />} />

      <Route path="/instructor/login" element={<InstructorLogin />} />
      <Route path="/instructor/register" element={<InstructorRegister />} />

      <Route path="/admin/login" element={<AdminLogin />} />

      {/* ---------- STUDENT ---------- */}
      <Route
        path="/student/dashboard"
        element={
          <ProtectedRoute role="STUDENT">
            <StudentDashboard />
          </ProtectedRoute>
        }
      />

      <Route
        path="/student/my-courses"
        element={
          <ProtectedRoute role="STUDENT">
            <StudentMyCourses />
          </ProtectedRoute>
        }
      />

      <Route
        path="/student/course/:courseId/videos"
        element={
          <ProtectedRoute role="STUDENT">
            <StudentCourseVideos />
          </ProtectedRoute>
        }
      />

      {/* ---------- INSTRUCTOR ---------- */}
      <Route
        path="/instructor"
        element={
          <ProtectedRoute role="INSTRUCTOR">
            <InstructorLayout />
          </ProtectedRoute>
        }
      >
        <Route path="dashboard" element={<InstructorDashboard />} />
        <Route path="courses" element={<InstructorCourses />} />
        <Route
          path="course/:courseId/videos"
          element={<InstructorCourseVideos />}
        />
      </Route>

      {/* ---------- ADMIN ---------- */}
      <Route
        path="/admin/dashboard"
        element={
          <ProtectedRoute role="ADMIN">
            <AdminDashboard />
          </ProtectedRoute>
        }
      />

    </Routes>
  );
}

export default App;
