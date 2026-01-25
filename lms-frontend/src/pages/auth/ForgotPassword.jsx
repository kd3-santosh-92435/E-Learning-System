import { useState } from "react";
import { toast } from "react-toastify";
import { forgotPasswordApi } from "../../features/auth/authApi";
import "./Auth.css";

const ForgotPassword = () => {
  const [email, setEmail] = useState("");
  const [loading, setLoading] = useState(false);

  const submit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      await forgotPasswordApi(email);
      toast.success("Password reset link sent to email");
    } catch (err) {
      toast.error(err.response?.data || "Email not registered");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <form className="auth-card" onSubmit={submit}>
        <h2>Forgot Password</h2>

        <input
          placeholder="Enter registered email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />

        <button disabled={loading}>
          {loading ? "Sending..." : "Send Reset Link"}
        </button>
      </form>
    </div>
  );
};

export default ForgotPassword;
