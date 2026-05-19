import React from 'react';
import { Routes, Route, Link } from 'react-router-dom';
import HotelManagement from './HotelManagement';
import RoomManagement from './RoomManagement';
import OccupancyReport from './OccupancyReport';

function AdminDashboard() {
  return (
    <div style={{ display: 'flex' }}>
      <aside style={{ width: '200px', padding: '20px', borderRight: '1px solid #ccc' }}>
        <ul>
          <li><Link to="/admin">Hotels</Link></li>
          <li><Link to="/admin/rooms">Rooms</Link></li>
          <li><Link to="/admin/report">Occupancy Report</Link></li>
        </ul>
      </aside>
      <main style={{ flex: 1, padding: '20px' }}>
        <Routes>
          <Route path="/" element={<HotelManagement />} />
          <Route path="/rooms" element={<RoomManagement />} />
          <Route path="/report" element={<OccupancyReport />} />
        </Routes>
      </main>
    </div>
  );
}

export default AdminDashboard;