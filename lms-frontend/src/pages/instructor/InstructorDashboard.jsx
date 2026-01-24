import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { logout } from "../../features/auth/authSlice";
import {
  createCourseApi,
  getInstructorCoursesApi,
} from "../../features/instructor/instructorApi";

import "../../styles/extreme.css";

const InstructorDashboard = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector((state) => state.auth.user);

  const [courses, setCourses] = useState([]);
  const [form, setForm] = useState({
    title: "",
    description: "",
    price: "",
  });

  const loadCourses = async () => {
    const res = await getInstructorCoursesApi();
    setCourses(res.data);
  };

  useEffect(() => {
    loadCourses();
  }, []);

  const handleCreateCourse = async (e) => {
    e.preventDefault();
    await createCourseApi(form);
    setForm({ title: "", description: "", price: "" });
    loadCourses();
  };

  const handleLogout = () => {
    dispatch(logout());
    navigate("/instructor/login");
  };

  return (
    <div className="container mt-5">
      <div className="glass">

        <div className="dashboard-header">
          <div>
            <h2>🎓 Instructor Dashboard</h2>
            <p>Welcome, <b>{user?.email}</b></p>
          </div>

          <button className="btn btn-outline-light" onClick={handleLogout}>
            Logout
          </button>
        </div>

        <hr />

        <h4>📚 Create New Course</h4>

        <form onSubmit={handleCreateCourse}>
          <input
            className="form-control mb-2"
            placeholder="Course Title"
            value={form.title}
            onChange={(e) => setForm({ ...form, title: e.target.value })}
            required
          />

          <textarea
            className="form-control mb-2"
            placeholder="Course Description"
            value={form.description}
            onChange={(e) => setForm({ ...form, description: e.target.value })}
            required
          />

          <input
            type="number"
            className="form-control mb-3"
            placeholder="Price (₹)"
            value={form.price}
            onChange={(e) => setForm({ ...form, price: e.target.value })}
            required
          />

          <button className="btn btn-gradient w-100">
            ➕ Create Course
          </button>
        </form>

      </div>
    </div>
  );
};

export default InstructorDashboard;
