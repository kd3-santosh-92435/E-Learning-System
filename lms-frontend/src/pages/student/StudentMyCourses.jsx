import React, { useEffect, useState } from "react";
import { toast } from "react-toastify";
import { getMyCoursesApi } from "../../services/studentApi";
import { useNavigate } from "react-router-dom";

const StudentMyCourses = () => {
  const [courses, setCourses] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    loadMyCourses();
  }, []);

  const loadMyCourses = async () => {
    try {
      const res = await getMyCoursesApi();
      setCourses(res.data);
    } catch (err) {
      toast.error("Failed to load enrolled courses");
    }
  };

  return (
    <div className="instructor-courses-page">
      <div className="dashboard-header">
        <h1 className="page-title">📘 My Courses</h1>

        <button
          className="btn btn-outline-light"
          onClick={() => navigate("/student/dashboard")}
        >
          ⬅ Back
        </button>
      </div>

      <div className="course-grid">
        {courses.length === 0 && (
          <p style={{ color: "white" }}>
            You have not enrolled in any courses yet
          </p>
        )}

        {courses.map(course => (
          <div className="course-card" key={course.courseId}>
            <h3>{course.title}</h3>
            <p>{course.description}</p>

            <button
              className="video"
              onClick={() =>
                navigate(`/student/course/${course.courseId}/videos`)
              }
            >
              ▶ View Videos
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default StudentMyCourses;
