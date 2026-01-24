import { useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { jwtDecode } from "jwt-decode";

import "../../styles/auth.css";
import { adminLoginApi } from "../../features/auth/authApi";
import { loginSuccess } from "../../features/auth/authSlice";

const AdminLogin = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await adminLoginApi({ email, password });
      const decoded = jwtDecode(res.data.token);

      dispatch(
        loginSuccess({
          token: res.data.token,
          user: { email: decoded.sub, role: "ADMIN" },
        })
      );

      toast.success("Admin login successful");
      navigate("/admin/dashboard", { replace: true });
    } catch {
      toast.error("Invalid credentials");
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2>Admin Login</h2>

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

export default AdminLogin;
