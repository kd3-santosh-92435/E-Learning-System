import { useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { jwtDecode } from "jwt-decode";

import "../../styles/auth.css";
import { studentLoginApi } from "../../features/auth/authApi";
import { loginSuccess } from "../../features/auth/authSlice";

const StudentLogin = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await studentLoginApi({ email, password });
      const decoded = jwtDecode(res.data.token);

      dispatch(
        loginSuccess({
          token: res.data.token,
          user: { email: decoded.sub, role: "STUDENT" },
        })
      );

      toast.success("Student login successful");
      navigate("/student/dashboard", { replace: true });
    } catch {
      toast.error("Invalid email or password");
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2>Student Login</h2>

        <form onSubmit={handleSubmit}>
          <input
            placeholder="Email"
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <input
            type="password"
            placeholder="Password"
            onChange={(e) => setPassword(e.target.value)}
            required
          />

          <button>Login</button>
        </form>
      </div>
    </div>
  );
};

export default StudentLogin;
