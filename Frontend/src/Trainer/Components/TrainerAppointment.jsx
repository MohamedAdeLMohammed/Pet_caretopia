import "../CSS/TrainerDashboard.css";
import { useState } from "react";
import { Link } from "react-router-dom";

function TrainerAppointment() {
      const [days, setDays] = useState([
        { id: 1, date: "2025-04-15", timeRange: "10:00 AM - 2:00 PM" },
        { id: 2, date: "2025-04-16", timeRange: "12:00 PM - 4:00 PM" },
        { id: 3, date: "2025-04-17", timeRange: "9:00 AM - 1:00 PM" },
      ]);
    
      const [showForm, setShowForm] = useState(false);
      const [newDate, setNewDate] = useState("");
      const [newTimeRange, setNewTimeRange] = useState("");
    
      const handleAddDay = () => {
        setShowForm(true);
      };
    
      const handleSubmit = (e) => {
        e.preventDefault();
        const newDay = {
          id: days.length + 1,
          date: newDate,
          timeRange: newTimeRange,
        };
        setDays([...days, newDay]);
        setNewDate("");
        setNewTimeRange("");
        setShowForm(false);
      };
    
      return (
        <div className="overview-container">
          <h2 className="overview-title">Scheduled Appointments</h2>
    
          <table className="overview-table">
            <thead>
              <tr>
                <th>Date</th>
                <th>Time Range</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {days.map((day) => (
                <tr key={day.id}>
                  <td>{day.date}</td>
                  <td>{day.timeRange}</td>
                  <td>
                  <Link to={"/TrainerDashboard/ViewTrainerAppointment"} className="view-btn">
                   <p>View Appointments</p>
                    </Link>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
    
          <div className="add-day-container">
            <button className="add-day-btn" onClick={handleAddDay}>
              + Add New Appointment Day
            </button>
          </div>
    
          {showForm && (
            <div className="popup-form-overlay">
              <div className="popup-form">
                 <h4 className="form-label">Add New Appointment Day</h4>
                <form onSubmit={handleSubmit}>
                  <label>Date:</label>
                  <input
                    type="date"
                    value={newDate}
                    onChange={(e) => setNewDate(e.target.value)}
                    required
                  />
    
                  <label>Time Range:</label>
                  <input
                    type="text"
                    placeholder="e.g. 10:00 AM - 2:00 PM"
                    value={newTimeRange}
                    onChange={(e) => setNewTimeRange(e.target.value)}
                    required
                  />
    
                  <div className="popup-buttons">
                    <button type="submit" className="sub-btn">Submit</button>
                    <button type="button" onClick={() => setShowForm(false)} className="cancel-btn">Cancel</button>
                  </div>
                </form>
              </div>
            </div>
          )}
        </div>
      );
    }
    
    
export default TrainerAppointment;
