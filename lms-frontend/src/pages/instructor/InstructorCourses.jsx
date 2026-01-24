import React, { useEffect, useState } from "react";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import courseService from "../../services/instructorCourseService";
import "./InstructorCourses.css";

const InstructorCourses = () => {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);

  /* ===== EDIT STATE ===== */
  const [showEdit, setShowEdit] = useState(false);
  const [editCourse, setEditCourse] = useState({
    courseId: "",
    title: "",
    description: "",
    price: "",
  });

  const navigate = useNavigate();

  useEffect(() => {
    loadCourses();
  }, []);

  /* ================= LOAD COURSES ================= */
  const loadCourses = async () => {
    try {
      const res = await courseService.getMyCourses();
      setCourses(res.data);
    } catch (err) {
      toast.error("Failed to load courses");
    } finally {
      setLoading(false);
    }
  };

  /* ================= DELETE COURSE ================= */
  const deleteCourse = async (courseId) => {
    if (!window.confirm("Delete this course?")) return;

    try {
      await courseService.deleteCourse(courseId);
      toast.success("Course deleted");
      loadCourses();
    } catch {
      toast.error("Delete failed");
    }
  };

  /* ================= OPEN EDIT ================= */
  const openEditModal = (course) => {
    setEditCourse({
      courseId: course.courseId,
      title: course.title,
      description: course.description,
      price: course.price,
    });
    setShowEdit(true);
  };

  /* ================= UPDATE COURSE ================= */
  const handleUpdate = async (e) => {
    e.preventDefault();

    try {
      await courseService.updateCourse(editCourse.courseId, {
        title: editCourse.title,
        description: editCourse.description,
        price: editCourse.price,
      });

      toast.success("Course updated");
      setShowEdit(false);
      loadCourses();
    } catch {
      toast.error("Update failed");
    }
  };

  if (loading) return <h2 style={{ color: "white" }}>Loading...</h2>;

  return (
    <div className="instructor-courses-page">
      <h1 className="page-title">📚 My Courses</h1>

      <div className="course-grid">
        {courses.map((course) => (
          <div className="course-card" key={course.courseId}>
            <h3>{course.title}</h3>
            <p>{course.description}</p>
            <p className="price">₹ {course.price}</p>

            <div className="actions">
              <button
                className="edit"
                onClick={() => openEditModal(course)}
              >
                Edit
              </button>

              <button
                className="delete"
                onClick={() => deleteCourse(course.courseId)}
              >
                Delete
              </button>

              <button
                className="video"
                onClick={() =>
                  navigate(`/instructor/course/${course.courseId}/videos`)
                }
              >
                Manage Videos
              </button>
            </div>
          </div>
        ))}
      </div>

      {/* ================= EDIT MODAL ================= */}
      {showEdit && (
        <div className="modal-overlay">
          <div className="modal">
            <h3>Edit Course</h3>

            <form onSubmit={handleUpdate}>
              <input
                value={editCourse.title}
                onChange={(e) =>
                  setEditCourse({ ...editCourse, title: e.target.value })
                }
                placeholder="Title"
                required
              />

              <textarea
                value={editCourse.description}
                onChange={(e) =>
                  setEditCourse({
                    ...editCourse,
                    description: e.target.value,
                  })
                }
                placeholder="Description"
                required
              />

              <input
                type="number"
                value={editCourse.price}
                onChange={(e) =>
                  setEditCourse({ ...editCourse, price: e.target.value })
                }
                placeholder="Price"
                required
              />

              <div className="modal-actions">
                <button type="submit" className="save">
                  Save
                </button>
                <button
                  type="button"
                  className="cancel"
                  onClick={() => setShowEdit(false)}
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default InstructorCourses;
