import { Link } from "react-router-dom";
import "./LandingPage.css";

const LandingPage = () => {
  return (
    <div className="landing">
      <h1 className="title">E-Learning System</h1>
      <p className="subtitle">
        Learn skills. Teach globally. Manage everything in one platform.
      </p>

      <div className="role-section">
        <div className="card">
          <h2>Student</h2>
          <Link to="/student/login">Login</Link>
          <Link to="/student/register">Register</Link>
        </div>

        <div className="card">
          <h2>Instructor</h2>
          <Link to="/instructor/login">Login</Link>
          <Link to="/instructor/register">Register</Link>
        </div>

        <div className="card">
          <h2>Admin</h2>
          <Link to="/admin/login">Login</Link>
        </div>
      </div>
    </div>
  );
};

export default LandingPage;
