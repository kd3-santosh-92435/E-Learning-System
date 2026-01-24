import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { logout } from "../features/auth/authSlice";

const SessionGuard = ({ children }) => {
  const dispatch = useDispatch();

  useEffect(() => {
    window.onpopstate = () => {
      dispatch(logout());
    };
  }, []);

  return children;
};

export default SessionGuard;
