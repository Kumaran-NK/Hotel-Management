import React, { useState, useEffect } from 'react';
import api from '../api';

function MyPayments({ user }) {
  const [payments, setPayments] = useState([]);

  useEffect(() => {
    fetchPayments();
  }, []);

  const fetchPayments = async () => {
    const res = await api.get('/payments/my');
    setPayments(res.data);
  };

  return (
    <div>
      <h2>My Payments</h2>
      {payments.length === 0 && <p>No payments found.</p>}
      <table border="1" cellPadding="5">
        <thead><tr><th>Payment ID</th><th>Booking ID</th><th>Amount</th><th>Method</th><th>Status</th><th>Date</th></tr></thead>
        <tbody>
          {payments.map(p => (
            <tr key={p.paymentId}>
              <td>{p.paymentId}</td>
              <td>{p.booking?.bookingId}</td>
              <td>${p.amount}</td>
              <td>{p.paymentMethod}</td>
              <td>{p.paymentStatus}</td>
              <td>{p.transactionDate}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default MyPayments;