import React from "react";
import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { logout } from "../features/auth/authSlice";
import "./InstructorLayout.css";

const InstructorLayout = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleLogout = () => {
    dispatch(logout());
    navigate("/instructor/login");
  };

  return (
    <div className="instructor-layout">
      {/* SIDEBAR */}
      <aside className="sidebar">
        <h2>Instructor</h2>

        <NavLink to="/instructor/dashboard">Dashboard</NavLink>
        <NavLink to="/instructor/courses">My Courses</NavLink>

        <button className="logout-btn" onClick={handleLogout}>
          Logout
        </button>
      </aside>

      {/* MAIN CONTENT */}
      <main className="content">
        <Outlet />
      </main>
    </div>
  );
};

export default InstructorLayout;
