import React, { useState } from 'react';
import api from '../api';

function SearchRooms({ user }) {
  const [city, setCity] = useState('');
  const [checkIn, setCheckIn] = useState('');
  const [checkOut, setCheckOut] = useState('');
  const [capacity, setCapacity] = useState(1);
  const [rooms, setRooms] = useState([]);
  const [bookingMsg, setBookingMsg] = useState('');

  const search = async () => {
    const res = await api.get('/customer/rooms/available', { params: { city, checkIn, checkOut, capacity } });
    setRooms(res.data);
  };

  const bookRoom = async (roomId) => {
    try {
      const res = await api.post('/customer/bookings', null, { params: { roomId, checkIn, checkOut } });
      setBookingMsg(`Booking created! ID: ${res.data.bookingId}, Total: $${res.data.totalAmount}`);
      search(); // refresh availability
    } catch (err) {
      setBookingMsg('Error: ' + (err.response?.data || 'Booking failed'));
    }
  };

  return (
    <div>
      <h2>Search Available Rooms</h2>
      <div>
        <input placeholder="City" value={city} onChange={e => setCity(e.target.value)} />
        <label>Check-in:</label><input type="date" value={checkIn} onChange={e => setCheckIn(e.target.value)} />
        <label>Check-out:</label><input type="date" value={checkOut} onChange={e => setCheckOut(e.target.value)} />
        <label>Min Capacity:</label><input type="number" value={capacity} onChange={e => setCapacity(e.target.value)} />
        <button onClick={search}>Search</button>
      </div>
      {bookingMsg && <p>{bookingMsg}</p>}
      {rooms.length > 0 && (
        <table border="1" cellPadding="5" style={{ marginTop: '20px' }}>
          <thead><tr><th>Room#</th><th>Type</th><th>Capacity</th><th>Base Price</th><th>Hotel</th><th>Action</th></tr></thead>
          <tbody>
            {rooms.map(r => (
              <tr key={r.roomId}>
                <td>{r.roomNumber}</td><td>{r.roomType}</td><td>{r.capacity}</td><td>${r.basePrice}</td>
                <td>{r.hotel?.hotelName}</td>
                <td><button onClick={() => bookRoom(r.roomId)}>Book</button></td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default SearchRooms;