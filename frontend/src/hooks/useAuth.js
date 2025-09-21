import { useState, useEffect } from 'react';
import axios from 'axios';

const useAuth = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState(null);
  const [userRole, setUserRole] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        // The backend session cookie will be sent automatically with this request
        const response = await axios.get(`${process.env.REACT_APP_API_URL}/api/auth/me`, { withCredentials: true });
        if (response.status === 200) {
          setIsAuthenticated(true);
          setUser(response.data);
          setUserRole(response.data.role);
        } else {
          setIsAuthenticated(false);
          setUser(null);
          setUserRole(null);
        }
      } catch (error) {
        setIsAuthenticated(false);
        setUser(null);
        setUserRole(null);
      } finally {
        setIsLoading(false);
      }
    };

    fetchUser();
  }, []);

  return { isAuthenticated, user, userRole, isLoading };
};

export default useAuth;