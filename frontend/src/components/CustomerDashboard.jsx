import React from 'react';
import { Routes, Route, Link } from 'react-router-dom';
import SearchRooms from './SearchRooms';
import MyBookings from './MyBookings';
import MyPayments from './MyPayments';

function CustomerDashboard({ user }) {
  return (
    <div style={{ display: 'flex' }}>
      <aside style={{ width: '200px', padding: '20px', borderRight: '1px solid #ccc' }}>
        <ul>
          <li><Link to="/customer">Search Rooms</Link></li>
          <li><Link to="/customer/bookings">My Bookings</Link></li>
          <li><Link to="/customer/payments">My Payments</Link></li>
        </ul>
      </aside>
      <main style={{ flex: 1, padding: '20px' }}>
        <Routes>
          <Route path="/" element={<SearchRooms user={user} />} />
          <Route path="/bookings" element={<MyBookings user={user} />} />
          <Route path="/payments" element={<MyPayments user={user} />} />
        </Routes>
      </main>
    </div>
  );
}

export default CustomerDashboard;