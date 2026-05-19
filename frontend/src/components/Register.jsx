import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api';

function Register() {
  const [form, setForm] = useState({
    customerName: '',
    email: '',
    phoneNumber: '',
    address: '',
    password: ''
  });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/auth/register', form);
      alert('Registration successful! Please login.');
      navigate('/login');
    } catch (err) {
      setError(err.response?.data || 'Registration failed');
    }
  };

  return (
    <div style={{ maxWidth: '400px', margin: '50px auto' }}>
      <h2>Register (Customer)</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <form onSubmit={handleSubmit}>
        <div><label>Full Name: </label><input name="customerName" value={form.customerName} onChange={handleChange} required /></div>
        <div><label>Email: </label><input name="email" type="email" value={form.email} onChange={handleChange} required /></div>
        <div><label>Phone: </label><input name="phoneNumber" value={form.phoneNumber} onChange={handleChange} required /></div>
        <div><label>Address: </label><input name="address" value={form.address} onChange={handleChange} required /></div>
        <div><label>Password: </label><input name="password" type="password" value={form.password} onChange={handleChange} required /></div>
        <button type="submit">Register</button>
      </form>
    </div>
  );
}

export default Register;