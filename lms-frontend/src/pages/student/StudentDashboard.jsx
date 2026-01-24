import React, { useEffect, useState } from "react";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import {
  getAllCoursesApi,
  createOrderApi,
  verifyPaymentApi,
} from "../../services/studentApi";

const StudentDashboard = () => {
  const [courses, setCourses] = useState([]);
  const [loadingId, setLoadingId] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    loadCourses();
  }, []);

  /* ================= LOAD COURSES ================= */
  const loadCourses = async () => {
    try {
      const res = await getAllCoursesApi();
      setCourses(res.data);
    } catch (err) {
      console.error(err);
      toast.error("Failed to load courses");
    }
  };

  /* ================= PAY & ENROLL ================= */
  const enrollCourse = async (course) => {
    if (loadingId) return; // ⛔ prevent double click

    try {
      setLoadingId(course.courseId);

      // 1️⃣ Create Razorpay Order
      const res = await createOrderApi(course.courseId);
      const { orderId, amount, currency } = res.data;

      // 2️⃣ Razorpay Options
      const options = {
        key: import.meta.env.VITE_RAZORPAY_KEY,
        amount,
        currency,
        name: "E-Learning Platform",
        description: course.title,
        order_id: orderId,

        handler: async function (response) {
          try {
            // 3️⃣ Verify Payment
            const verifyRes = await verifyPaymentApi({
              razorpayOrderId: response.razorpay_order_id,
              razorpayPaymentId: response.razorpay_payment_id,
              razorpaySignature: response.razorpay_signature,
            });

            toast.success("🎉 Payment successful & enrolled!");

            // 4️⃣ Remove enrolled course from list
            setCourses((prev) =>
              prev.filter((c) => c.courseId !== course.courseId)
            );

          } catch (err) {
            console.error(err);
            toast.error("Payment verification failed");
          }
        },

        modal: {
          ondismiss: function () {
            toast.info("Payment cancelled");
          },
        },

        theme: {
          color: "#6f42c1",
        },
      };

      const razorpay = new window.Razorpay(options);
      razorpay.open();

    } catch (err) {
      console.error(err);
      toast.error("Failed to initiate payment");
    } finally {
      setLoadingId(null);
    }
  };

  return (
    <div className="instructor-courses-page">
      {/* HEADER */}
      <div className="dashboard-header">
        <h1 className="page-title">🎓 Student Dashboard</h1>

        <button
          className="btn btn-outline-light"
          onClick={() => navigate("/student/my-courses")}
        >
          📘 My Courses
        </button>
      </div>

      {/* COURSE GRID */}
      <div className="course-grid">
        {courses.length === 0 && (
          <p style={{ color: "white", fontSize: "18px" }}>
            🎉 You have enrolled in all available courses
          </p>
        )}

        {courses.map((c) => (
          <div
            className={`course-card ${
              loadingId === c.courseId ? "disabled" : ""
            }`}
            key={c.courseId}
          >
            <h3>{c.title}</h3>
            <p>{c.description}</p>
            <p className="price">₹ {c.price}</p>

            <button
              className="video"
              disabled={loadingId === c.courseId}
              onClick={() => enrollCourse(c)}
            >
              {loadingId === c.courseId
                ? "Processing..."
                : "Enroll & Pay"}
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default StudentDashboard;
