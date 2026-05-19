import React, { useState } from 'react';
import api from '../api';

function OccupancyReport() {
  const [start, setStart] = useState('');
  const [end, setEnd] = useState('');
  const [report, setReport] = useState([]);

  const fetchReport = async () => {
    const res = await api.get('/admin/reports/occupancy-revenue', { params: { start, end } });
    setReport(res.data);
  };

  return (
    <div>
      <h2>Occupancy & Revenue Report (per room type)</h2>
      <div>
        <label>Start Date: </label><input type="date" value={start} onChange={e => setStart(e.target.value)} />
        <label>End Date: </label><input type="date" value={end} onChange={e => setEnd(e.target.value)} />
        <button onClick={fetchReport}>Generate</button>
      </div>
      {report.length > 0 && (
        <table border="1" cellPadding="5" style={{ marginTop: '20px' }}>
          <thead><tr><th>Hotel Name</th><th>Room Type</th><th>Occupancy Rate</th><th>Revenue</th></tr></thead>
          <tbody>
            {report.map((row, idx) => (
              <tr key={idx}>
                <td>{row[0]}</td><td>{row[1]}</td><td>{row[2]}</td><td>{row[3]}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default OccupancyReport;