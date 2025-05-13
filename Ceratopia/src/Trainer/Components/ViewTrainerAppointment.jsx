import { useState } from "react";
function ViewTrainerAppointment() {
  const [appointments, setAppointments] = useState([
    { id: 1, petType: 'max', date: '2025-04-15', time: '10:00 AM',age:'6 months',breed:"german", status: 'Booked' },
    { id: 2, petType: 'max', date: '2025-04-15', time: '10:00 AM',age:'6 months',breed:"german", status: 'Booked' },
    { id: 3, petType: 'max', date: '2025-04-15', time: '10:00 AM',age:'6 months',breed:"german", status: 'Booked' },
  ]);

  const cancelAppointment = (id) => {
    setAppointments(appointments.filter(appointment => appointment.id !== id));
  };

  const approveAppointment = (id) => {
    setAppointments(appointments.map(appointment =>
      appointment.id === id ? { ...appointment, status: 'Approved' } : appointment
    ));
  };

  return (
    <div className="vet-dashboard">
      
      <div className="appointments-container">
        <h2>Appointments</h2>
        <table className="appointments-table">
          <thead>
            <tr>
              <th>Pet Type </th>
              <th>Date</th>
              <th>Time</th>
              <th>Age</th>
              <th>Breed</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {appointments.map((appointment) => (
              <tr key={appointment.id}>
                <td>{appointment.petType}</td>
                <td>{appointment.date}</td>
                <td>{appointment.time}</td>
                <td>{appointment.age}</td>
                <td>{appointment.breed}</td>
                <td>{appointment.status}</td>
                <td>
                {appointment.status !== 'Approved' && (
                    <button onClick={() => approveAppointment(appointment.id)} className="approve-btn">
                      Approve
                    </button>
                  )}
                  <button onClick={() => cancelAppointment(appointment.id)} className="cancel-btn">
                    Cancel
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default ViewTrainerAppointment;
