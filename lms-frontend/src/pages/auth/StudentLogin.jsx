import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { toast } from "react-toastify";
import api from "../../services/api";
import "./Auth.css";

const StudentLogin = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const submit = async (e) => {
    e.preventDefault();

    if (!email || !password) {
      toast.error("All fields required");
      return;
    }

    try {
      const res = await api.post("/auth/student/login", {
        email,
        password,
      });

      localStorage.setItem("token", res.data.token);
      localStorage.setItem("role", "STUDENT");

      toast.success("Login successful");
      navigate("/student/dashboard");
    } catch {
      toast.error("Invalid credentials");
    }
  };

  return (
    <div className="auth-container">
      <form className="auth-card" onSubmit={submit}>
        <h2>Student Login</h2>

        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <button>Login</button>

        <div className="auth-links">
          <Link to="/forgot-password">Forgot password?</Link>
          <br />
          <Link to="/student/register">
            New here? Register
          </Link>
        </div>
      </form>
    </div>
  );
};

export default StudentLogin;
