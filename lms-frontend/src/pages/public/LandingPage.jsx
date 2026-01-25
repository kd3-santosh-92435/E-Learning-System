import { Link } from "react-router-dom";
import "./LandingPage.css";

const LandingPage = () => {
  return (
    <div className="landing">
      <nav className="landing-nav">
        <h2>UdemyClone</h2>
        <Link to="/student/login" className="nav-btn">Log in</Link>
      </nav>

      <div className="hero">
        <h1>Learn without limits</h1>
        <p>Build skills with courses taught by real instructors</p>

        <div className="hero-actions">
          <Link to="/student/register">Join as Student</Link>
          <Link to="/instructor/register">Teach on Platform</Link>
        </div>
      </div>

      <div className="role-section">
        <div className="card">
          <h3>Student</h3>
          <Link to="/student/login">Login</Link>
          <Link to="/forgot-password">Forgot Password</Link>
        </div>

        <div className="card">
          <h3>Instructor</h3>
          <Link to="/instructor/login">Login</Link>
          <Link to="/instructor/register">Register</Link>
        </div>

        <div className="card">
          <h3>Admin</h3>
          <Link to="/admin/login">Login</Link>
        </div>
      </div>
    </div>
  );
};

export default LandingPage;
