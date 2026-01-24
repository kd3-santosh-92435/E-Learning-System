import { Routes, Route } from "react-router-dom";
import InstructorDashboard from "../pages/instructor/InstructorDashboard";
import InstructorCourses from "../pages/instructor/InstructorCourses";
import InstructorCourseVideos from "../pages/instructor/InstructorCourseVideos";

const InstructorRoutes = () => {
  return (
    <Routes>
      <Route path="dashboard" element={<InstructorDashboard />} />
      <Route path="courses" element={<InstructorCourses />} />
      <Route path="course/:courseId/videos" element={<InstructorCourseVideos />} />
    </Routes>
  );
};

export default InstructorRoutes;
