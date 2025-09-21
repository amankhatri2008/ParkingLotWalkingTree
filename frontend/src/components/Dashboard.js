import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import useAuth from '../hooks/useAuth';

const Dashboard = () => {
  const { userRole, user } = useAuth(); // Get user role from the auth hook
  const navigate = useNavigate(); // Hook for programmatic navigation

  const [parkPlateNo, setParkPlateNo] = useState('');
  const [parkVehicleType, setParkVehicleType] = useState('CAR');
  const [parkTicket, setParkTicket] = useState(null);
  const [exitTicketId, setExitTicketId] = useState('');
  const [receipt, setReceipt] = useState(null);

  const parkVehicle = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(
        '/api/user/park',
        { plateNo: parkPlateNo, vehicleType: parkVehicleType },
        { withCredentials: true }
      );
      setParkTicket(response.data);
      alert('Vehicle parked successfully!');
    } catch (error) {
      alert('Failed to park vehicle: ' + (error.response?.data?.message || error.message));
    }
  };

  const exitVehicle = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(
        `${process.env.REACT_APP_API_URL}/api/user/exit`,
        { ticketId: exitTicketId },
        { withCredentials: true }
      );
      setReceipt(response.data);
      alert('Vehicle exited and payment processed!');
    } catch (error) {
      alert('Failed to exit vehicle: ' + (error.response?.data?.message || error.message));
    }
  };

  return (
    <div className="container">
    <img src="/Walkingtree-4-min.png" alt="Parking Lot Logo" style={{ display: 'block', margin: '0 auto', marginTop: '50px' }} />
      <h1 style={{ textAlign: 'center' }}>User Dashboard  -- {user?.name}</h1>
      
      {userRole === 'ADMIN' && (
        <button
          className="admin-button container"
          onClick={() => navigate('/admin')} style={{ textAlign: 'center' }}
        >
          Go to Admin Panel
        </button>
      )}
      
  <div style={{ marginBottom: '20px', padding: '15px' }}></div>
      {/* Park Vehicle Section */}
      <div className="card">
        <h3>Park a Vehicle</h3>
        <form onSubmit={parkVehicle}>
          <div className="form-group">
            <label>Vehicle Type:</label>
            <select
              className="form-control"
              value={parkVehicleType}
              onChange={(e) => setParkVehicleType(e.target.value)}
            >
              <option value="BIKE">BIKE</option>
              <option value="CAR">CAR</option>
              <option value="TRUCK">TRUCK</option>
            </select>
          </div>
          
          <div className="form-group">
            <label>License Plate No:</label>
            <input
              className="form-control"
              type="text"
              value={parkPlateNo}
              onChange={(e) => setParkPlateNo(e.target.value)}
              required
            />
          </div>
          
          <button type="submit" style={{ textAlign: 'center' }} className="button-primary">Park Vehicle</button>
        </form>
      </div>
  <div style={{ marginBottom: '20px', padding: '15px' }}></div>
      {parkTicket && (
        <div className="card">
          <h3>Your Parking Ticket</h3>
          <p><strong>Ticket ID:</strong> {parkTicket.id}</p>
          <p><strong>Vehicle Plate:</strong> {parkTicket.vehiclePlateNo}</p>
          <p><strong>Slot:</strong> {parkTicket.parkingSlot.id}</p>
          <p><strong>Entry Time:</strong> {new Date(parkTicket.entryTime).toLocaleString()}</p>
        </div>
      )}
  <div style={{ marginBottom: '20px', padding: '15px' }}></div>

      <div className="card">
        <h3>Exit a Vehicle</h3>
        <form onSubmit={exitVehicle}>
          <div className="form-group">
            <label>Ticket ID:</label>
            <input
              className="form-control"
              type="number"
              value={exitTicketId}
              onChange={(e) => setExitTicketId(e.target.value)}
              required
            />
          </div>
          <button type="submit" className="button-primary">Exit Vehicle</button>
        </form>
      </div>
  <div style={{ marginBottom: '20px', padding: '15px' }}></div>
      {receipt && (
        <div className="card receipt-card">
          <h3>Receipt 🧾</h3>
          <p><strong>Ticket ID:</strong> {receipt.ticketId}</p>
          <p><strong>Vehicle Plate:</strong> {receipt.plateNo}</p>
          <p><strong>Entry Time:</strong> {new Date(receipt.entryTime).toLocaleString()}</p>
          <p><strong>Exit Time:</strong> {new Date(receipt.exitTime).toLocaleString()}</p>
          <p><strong>Duration:</strong> {receipt.durationInMinutes} minutes</p>
          <p><strong>Total Amount:</strong> ${receipt.totalAmount}</p>
        </div>
      )}
    </div>
  );
};

export default Dashboard;