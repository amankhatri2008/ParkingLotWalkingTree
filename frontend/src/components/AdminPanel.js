import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import useAuth from '../hooks/useAuth';


const AdminPanel = () => {
  const { userRole, isLoading, user } = useAuth();
  const navigate = useNavigate();
  const [slot, setSlot] = useState({ slotType: 'CAR', floor: 1, capacity: 1 });
  const [pricingRule, setPricingRule] = useState({ vehicleType: 'CAR', basePrice: 2, hourlyRate: 3 });
  
  const [parkingStatus, setParkingStatus] = useState(null);


  useEffect(() => {
    fetchParkingStatus();
  }, []);



  const fetchParkingStatus = async () => {
    try {
      const response = await axios.get(`${process.env.REACT_APP_API_URL}/api/admin/status`, { withCredentials: true });
      setParkingStatus(response.data);
    } catch (error) {
      alert('Failed to fetch parking status.', true);
    }
  };

  
  const handleAddSlot = async (e) => {
    e.preventDefault();
    try {
      await axios.post(`${process.env.REACT_APP_API_URL}/api/admin/slots`, slot, { withCredentials: true });
      alert('Slot added successfully!', true);
      fetchParkingStatus(); // Refresh status
    } catch (error) {
      alert('Failed to add slot: ' + (error.response?.data?.message || error.message), true);
    }
  };

  const handleAddPricingRule = async (e) => {
    e.preventDefault();
    try {
      await axios.post(`${process.env.REACT_APP_API_URL}/api/admin/pricing-rules`, pricingRule, { withCredentials: true });
      alert('Pricing rule added successfully!', true);
    } catch (error) {
      alert('Failed to add pricing rule: ' + (error.response?.data?.message || error.message), true);
    }
  };

  if (isLoading) {
    return <div>Loading...</div>;
  }
  
  if (userRole !== 'ADMIN') {
    navigate('/dashboard');
    return null;
  }

  return (
    <div className="container">
    <img src="/Walkingtree-4-min.png" alt="Parking Lot Logo" style={{ display: 'block', margin: '0 auto', marginTop: '50px' }} />
      <h1 style={{ textAlign: 'center' }}>Admin Dashboard -- {user?.name}</h1>
        <button
          className="admin-button container"
          onClick={() => navigate('/dashboard')} style={{ textAlign: 'center' }}
        >
          Go to User Panel
        </button>

  <div style={{ marginBottom: '20px', padding: '15px' }}></div>
      {/* Parking Status Section */}
      {parkingStatus && (
        <div className="card">
          <h3>Parking Lot Status</h3>
          <p><strong>Total Slots:</strong> {parkingStatus.totalSlots}</p>
          <p><strong>Available Slots:</strong> {parkingStatus.availableSlots}</p>
          <p><strong>Occupied Slots:</strong> {parkingStatus.occupiedSlots}</p>
          <button className="button-primary" onClick={fetchParkingStatus}>Refresh Status</button>
        </div>
      )}
  <div style={{ marginBottom: '20px', padding: '15px' }}></div>
      {/* Parked Vehicles Table */}
      {parkingStatus && parkingStatus.parkedVehicles.length > 0 && (
        <div className="card">
          <h3>Currently Parked Vehicles</h3>
          <table className="inventory-table">
            <thead>
              <tr>
                <th>Ticket ID</th>
                <th>Plate No.</th>
                <th>Slot ID</th>
                <th>Entry Time</th>
              </tr>
            </thead>
            <tbody>
              {parkingStatus.parkedVehicles.map((vehicle) => (
                <tr key={vehicle.ticketId}>
                  <td>{vehicle.ticketId}</td>
                  <td>{vehicle.plateNo}</td>
                  <td>{vehicle.slotId}</td>
                  <td>{new Date(vehicle.entryTime).toLocaleString()}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
  <div style={{ marginBottom: '20px', padding: '15px' }}></div>
      {/* Admin Forms Section (unchanged) */}
      <div className="card">
        <h3>Add Parking Slot</h3>
        <form onSubmit={handleAddSlot}>
          <div className="form-group">
            <label>Type:</label>
            <select
              className="form-control"
              value={slot.slotType}
              onChange={(e) => setSlot({ ...slot, slotType: e.target.value })}
            >
              <option value="BIKE">BIKE</option>
              <option value="CAR">CAR</option>
              <option value="TRUCK">TRUCK</option>
            </select>
          </div>
          <div className="form-group">
            <label>Floor:</label>
            <input
              className="form-control"
              type="number"
              value={slot.floor}
              onChange={(e) => setSlot({ ...slot, floor: e.target.value })}
            />
          </div>
          <button type="submit" className="button-primary">Add Slot</button>
        </form>
      </div>
  <div style={{ marginBottom: '20px', padding: '15px' }}></div>
      <div className="card">
        <h3>Add Pricing Rule</h3>
        <form onSubmit={handleAddPricingRule}>
          <div className="form-group">
            <label>Vehicle Type:</label>
            <select
              className="form-control"
              value={pricingRule.vehicleType}
              onChange={(e) => setPricingRule({ ...pricingRule, vehicleType: e.target.value })}
            >
              <option value="BIKE">BIKE</option>
              <option value="CAR">CAR</option>
              <option value="TRUCK">TRUCK</option>
            </select>
          </div>
          <div className="form-group">
            <label>Base Price:</label>
            <input
              className="form-control"
              type="number"
              value={pricingRule.basePrice}
              onChange={(e) => setPricingRule({ ...pricingRule, basePrice: e.target.value })}
            />
          </div>
          <div className="form-group">
            <label>Hourly Rate:</label>
            <input
              className="form-control"
              type="number"
              value={pricingRule.hourlyRate}
              onChange={(e) => setPricingRule({ ...pricingRule, hourlyRate: e.target.value })}
            />
          </div>
          <button type="submit" className="button-primary">Add Rule</button>
        </form>
      </div>
    </div>
  );
};

export default AdminPanel;