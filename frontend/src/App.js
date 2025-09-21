import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import LoginPage from './components/LoginPage';
import Dashboard from './components/Dashboard';
import AdminPanel from './components/AdminPanel';
import  useAuth from './hooks/useAuth';

function App() {
  const { isAuthenticated, userRole,isLoading } = useAuth(); // Custom hook for auth state
if (isLoading) {
  return <div>Loading...</div>; // Show a loading indicator
}
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route
          path="/dashboard"
          element={isAuthenticated ? <Dashboard /> : <Navigate to="/login" />}
        />
        <Route
          path="/admin"
          element={isAuthenticated && userRole === 'ADMIN' ? <AdminPanel /> : <Navigate to="/login" />}
        />
        <Route path="/" element={<Navigate to="/dashboard" />} />
      </Routes>
    </Router> 
  );
}

export default App;