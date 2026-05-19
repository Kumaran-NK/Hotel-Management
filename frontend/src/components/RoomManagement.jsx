import React, { useState, useEffect } from 'react';
import api from '../api';

function RoomManagement() {
  const [rooms, setRooms] = useState([]);
  const [hotels, setHotels] = useState([]);
  const [newRoom, setNewRoom] = useState({ roomNumber: '', roomType: '', capacity: 1, basePrice: 0, hotelId: '' });

  useEffect(() => {
    fetchRooms();
    fetchHotels();
  }, []);

  const fetchRooms = async () => {
    const res = await api.get('/admin/rooms');
    setRooms(res.data);
  };
  const fetchHotels = async () => {
    const res = await api.get('/admin/hotels');
    setHotels(res.data);
  };

  const addRoom = async () => {
    const roomData = { ...newRoom, hotel: { hotelId: newRoom.hotelId } };
    await api.post('/admin/rooms', roomData);
    setNewRoom({ roomNumber: '', roomType: '', capacity: 1, basePrice: 0, hotelId: '' });
    fetchRooms();
  };

  const deleteRoom = async (id) => {
    await api.delete(`/admin/rooms/${id}`);
    fetchRooms();
  };

  return (
    <div>
      <h2>Rooms</h2>
      <div>
        <input placeholder="Room Number" value={newRoom.roomNumber} onChange={e => setNewRoom({...newRoom, roomNumber: e.target.value})} />
        <input placeholder="Type (DELUXE/SUITE)" value={newRoom.roomType} onChange={e => setNewRoom({...newRoom, roomType: e.target.value})} />
        <input placeholder="Capacity" type="number" value={newRoom.capacity} onChange={e => setNewRoom({...newRoom, capacity: parseInt(e.target.value)})} />
        <input placeholder="Base Price" type="number" value={newRoom.basePrice} onChange={e => setNewRoom({...newRoom, basePrice: parseFloat(e.target.value)})} />
        <select value={newRoom.hotelId} onChange={e => setNewRoom({...newRoom, hotelId: e.target.value})}>
          <option value="">Select Hotel</option>
          {hotels.map(h => <option key={h.hotelId} value={h.hotelId}>{h.hotelName}</option>)}
        </select>
        <button onClick={addRoom}>Add Room</button>
      </div>
      <table border="1" cellPadding="5" style={{ marginTop: '20px' }}>
        <thead><tr><th>ID</th><th>Room#</th><th>Type</th><th>Capacity</th><th>Price</th><th>Hotel</th><th>Action</th></tr></thead>
        <tbody>
          {rooms.map(r => (
            <tr key={r.roomId}>
              <td>{r.roomId}</td><td>{r.roomNumber}</td><td>{r.roomType}</td><td>{r.capacity}</td><td>{r.basePrice}</td>
              <td>{r.hotel?.hotelName}</td><td><button onClick={() => deleteRoom(r.roomId)}>Delete</button></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default RoomManagement;