import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

import { studentRegisterApi } from "../../features/auth/authApi";

const StudentRegister = () => {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      await studentRegisterApi({ name, email, password });
      toast.success("Student registered successfully");
      navigate("/student/login");
    } catch (err) {
      toast.error(err.response?.data || "Registration failed");
    }
  };

  return (
    <div className="container mt-5 text-white">
      <div className="glass col-md-4 mx-auto">
        <h3>Student Register</h3>

        <form onSubmit={handleSubmit}>
          <input
            className="form-control mb-3"
            placeholder="Name"
            onChange={(e) => setName(e.target.value)}
            required
          />

          <input
            className="form-control mb-3"
            placeholder="Email"
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <input
            type="password"
            className="form-control mb-3"
            placeholder="Password"
            onChange={(e) => setPassword(e.target.value)}
            required
          />

          <button className="btn btn-gradient w-100">
            Register
          </button>
        </form>
      </div>
    </div>
  );
};

export default StudentRegister;
