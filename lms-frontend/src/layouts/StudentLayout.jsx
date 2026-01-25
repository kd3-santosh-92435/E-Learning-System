import { Outlet, useNavigate } from "react-router-dom";
import "./StudentLayout.css";

const StudentLayout = () => {
  const navigate = useNavigate();

  const logout = () => {
    localStorage.clear();
    navigate("/");
  };

  return (
    <>
      <header className="student-navbar">
        <h2 onClick={() => navigate("/student/dashboard")}>
          UdemyClone
        </h2>

        <nav>
          <button onClick={() => navigate("/student/dashboard")}>
            Courses
          </button>
          <button onClick={() => navigate("/student/my-courses")}>
            My Learning
          </button>
          <button className="logout" onClick={logout}>
            Logout
          </button>
        </nav>
      </header>

      <main className="student-content">
        <Outlet />
      </main>
    </>
  );
};

export default StudentLayout;
