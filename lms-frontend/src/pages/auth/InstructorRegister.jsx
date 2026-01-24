import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { instructorRegisterApi } from "../../features/auth/authApi";

const InstructorRegister = () => {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await instructorRegisterApi({ name, email, password });
      toast.success("Instructor registered successfully");
      navigate("/instructor/login");
    } catch {
      toast.error("Registration failed");
    }
  };

  return (
    <div className="container mt-5 text-white">
      <div className="glass col-md-4 mx-auto">
        <h3>Instructor Registration</h3>

        <form onSubmit={handleSubmit}>
          <input className="form-control mb-3" placeholder="Name"
            onChange={(e) => setName(e.target.value)} />

          <input className="form-control mb-3" placeholder="Email"
            onChange={(e) => setEmail(e.target.value)} />

          <input type="password" className="form-control mb-3"
            placeholder="Password"
            onChange={(e) => setPassword(e.target.value)} />

          <button className="btn btn-gradient w-100">
            Register
          </button>
        </form>
      </div>
    </div>
  );
};

export default InstructorRegister;
