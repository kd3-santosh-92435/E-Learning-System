import LogoutButton from "../../components/common/LogoutButton";

const AdminDashboard = () => {
  return (
    <div className="container mt-5 text-white">
      <div className="glass">
        <div className="d-flex justify-content-between">
          <h3>Admin Dashboard</h3>
          <LogoutButton />
        </div>

        <p className="mt-3">
          - Manage students <br />
          - Manage instructors <br />
          - Manage courses <br />
          - View payments
        </p>
      </div>
    </div>
  );
};

export default AdminDashboard;
