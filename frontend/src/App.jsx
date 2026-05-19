import React, { useState, useEffect } from 'react';
import { Routes, Route, Navigate, Link } from 'react-router-dom';
import api from './api';
import Login from './components/Login';
import Register from './components/Register';
import AdminDashboard from './components/AdminDashboard';
import CustomerDashboard from './components/CustomerDashboard';

function App() {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);


  useEffect(() => {
   
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
    setLoading(false);
  }, []);

  const handleLogin = (userData) => {
    setUser(userData);
    localStorage.setItem('user', JSON.stringify(userData));
  };

  const handleLogout = async () => {
    await api.post('/auth/logout');
    setUser(null);
    localStorage.removeItem('user');
  };

  if (loading) return <div>Loading...</div>;

  return (
    <div>
      <nav style={{ padding: '10px', borderBottom: '1px solid #ccc' }}>
        {user ? (
          <>
            <span>Welcome, {user.customerName || user.email} ({user.role})</span>
            {' | '}
            <Link to={user.role === 'ADMIN' ? '/admin' : '/customer'}>Dashboard</Link>
            {' | '}
            <button onClick={handleLogout}>Logout</button>
          </>
        ) : (
          <>
            <Link to="/login">Login</Link> | <Link to="/register">Register</Link>
          </>
        )}
      </nav>

      <Routes>
        <Route path="/login" element={<Login onLogin={handleLogin} />} />
        <Route path="/register" element={<Register />} />
        <Route
          path="/admin/*"
          element={user?.role === 'ADMIN' ? <AdminDashboard /> : <Navigate to="/login" />}
        />
        <Route
          path="/customer/*"
          element={user?.role === 'CUSTOMER' ? <CustomerDashboard user={user} /> : <Navigate to="/login" />}
        />
        <Route path="/" element={<Navigate to={user ? (user.role === 'ADMIN' ? '/admin' : '/customer') : '/login'} />} />
      </Routes>
    </div>
  );
}

export default App;