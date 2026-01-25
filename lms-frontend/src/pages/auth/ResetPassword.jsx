import React, { useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import api from "../../services/api";
import "./Auth.css";

const ResetPassword = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  const token = searchParams.get("token");
  const [password, setPassword] = useState("");
  const [confirm, setConfirm] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!password || !confirm) {
      toast.error("All fields required");
      return;
    }

    if (password !== confirm) {
      toast.error("Passwords do not match");
      return;
    }

    try {
      setLoading(true);

      await api.post("/auth/reset-password", {
        token,
        newPassword: password,
      });

      toast.success("Password reset successful");
      navigate("/student/login");
    } catch (err) {
      toast.error("Invalid or expired link");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <form onSubmit={handleSubmit} className="auth-card">
        <h2>Reset Password</h2>

        <input
          type="password"
          placeholder="New password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <input
          type="password"
          placeholder="Confirm password"
          value={confirm}
          onChange={(e) => setConfirm(e.target.value)}
        />

        <button disabled={loading}>
          {loading ? "Updating..." : "Reset Password"}
        </button>
      </form>
    </div>
  );
};

export default ResetPassword;
