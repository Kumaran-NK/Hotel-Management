import React, { useState, useEffect } from 'react';
import api from '../api';

function MyBookings({ user }) {
  const [bookings, setBookings] = useState([]);

  useEffect(() => {
    fetchBookings();
  }, []);

  const fetchBookings = async () => {
    const res = await api.get('/customer/bookings');
    setBookings(res.data);
  };

  const cancelBooking = async (id) => {
    if (window.confirm('Cancel this booking? Refund rules apply.')) {
      await api.post(`/customer/bookings/${id}/cancel`);
      fetchBookings();
    }
  };

  return (
    <div>
      <h2>My Bookings</h2>
      {bookings.length === 0 && <p>No bookings found.</p>}
      {bookings.map(b => (
        <div key={b.bookingId} style={{ border: '1px solid #ccc', margin: '10px', padding: '10px' }}>
          <p>Booking ID: {b.bookingId}</p>
          <p>Room: {b.room?.roomNumber} ({b.room?.roomType})</p>
          <p>Hotel: {b.room?.hotel?.hotelName}</p>
          <p>Dates: {b.checkInDate} to {b.checkOutDate}</p>
          <p>Total: ${b.totalAmount}</p>
          <p>Status: {b.bookingStatus}</p>
          {b.bookingStatus === 'CONFIRMED' && (
            <button onClick={() => cancelBooking(b.bookingId)}>Cancel</button>
          )}
        </div>
      ))}
    </div>
  );
}

export default MyBookings;