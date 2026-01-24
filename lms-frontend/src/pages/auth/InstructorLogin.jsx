import { useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import { toast } from "react-toastify";

import { instructorLoginApi } from "../../features/auth/authApi";
import { loginSuccess } from "../../features/auth/authSlice";

import "../../styles/auth.css";

const InstructorLogin = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const res = await instructorLoginApi({ email, password });
      const decoded = jwtDecode(res.data.token);

      dispatch(
        loginSuccess({
          token: res.data.token,
          user: {
            id: decoded.id,
            email: decoded.sub,
            role: decoded.role,
          },
        })
      );

      toast.success("Instructor login successful");
      navigate("/instructor/dashboard");
    } catch {
      toast.error("Invalid email or password");
    }
  };

  return (
    <div className="auth-container">
      <div className="glass auth-card">
        <h3 className="auth-title">Instructor Login</h3>

        <form onSubmit={handleSubmit}>
          <input
            className="form-control auth-input"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <input
            type="password"
            className="form-control auth-input"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />

          <button className="btn btn-gradient w-100">
            Login
          </button>
        </form>
      </div>
    </div>
  );
};

export default InstructorLogin;
