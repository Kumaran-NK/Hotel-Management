import React, { useState, useEffect } from 'react';
import api from '../api';

function HotelManagement() {
  const [hotels, setHotels] = useState([]);
  const [newHotel, setNewHotel] = useState({ hotelName: '', location: '', city: '', address: '', contactNumber: '', rating: 0 });

  useEffect(() => {
    fetchHotels();
  }, []);

  const fetchHotels = async () => {
    const res = await api.get('/admin/hotels');
    setHotels(res.data);
  };

  const addHotel = async () => {
    await api.post('/admin/hotels', newHotel);
    setNewHotel({ hotelName: '', location: '', city: '', address: '', contactNumber: '', rating: 0 });
    fetchHotels();
  };

  const deleteHotel = async (id) => {
    await api.delete(`/admin/hotels/${id}`);
    fetchHotels();
  };

  return (
    <div>
      <h2>Hotels</h2>
      <div>
        <input placeholder="Name" value={newHotel.hotelName} onChange={e => setNewHotel({...newHotel, hotelName: e.target.value})} />
        <input placeholder="Location" value={newHotel.location} onChange={e => setNewHotel({...newHotel, location: e.target.value})} />
        <input placeholder="City" value={newHotel.city} onChange={e => setNewHotel({...newHotel, city: e.target.value})} />
        <input placeholder="Address" value={newHotel.address} onChange={e => setNewHotel({...newHotel, address: e.target.value})} />
        <input placeholder="Contact" value={newHotel.contactNumber} onChange={e => setNewHotel({...newHotel, contactNumber: e.target.value})} />
        <input placeholder="Rating" type="number" step="0.1" value={newHotel.rating} onChange={e => setNewHotel({...newHotel, rating: parseFloat(e.target.value)})} />
        <button onClick={addHotel}>Add Hotel</button>
      </div>
      <table border="1" cellPadding="5" style={{ marginTop: '20px' }}>
        <thead><tr><th>ID</th><th>Name</th><th>City</th><th>Contact</th><th>Rating</th><th>Action</th></tr></thead>
        <tbody>
          {hotels.map(h => (
            <tr key={h.hotelId}>
              <td>{h.hotelId}</td><td>{h.hotelName}</td><td>{h.city}</td><td>{h.contactNumber}</td><td>{h.rating}</td>
              <td><button onClick={() => deleteHotel(h.hotelId)}>Delete</button></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default HotelManagement;