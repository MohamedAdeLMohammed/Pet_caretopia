import { useEffect, useState } from "react";
import axios from 'axios';
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
import { jwtDecode } from 'jwt-decode';
import clinic from '../../../assets/clinic.jpg';

function ClinicAppointments() {
    const [facilities, setFacilities] = useState([]);
    const token = sessionStorage.getItem('token');
    const navigate = useNavigate();
    const appointmentDuration = 0.5; // in hours
    let userId = null;

    if (token) {
        try {
            const decode = jwtDecode(token);
            userId = decode.id;
        } catch (error) {
            console.error("Invalid token", error);
            sessionStorage.removeItem('token');
        }
    }

    useEffect(() => {
        const getFacilities = async () => {
            try {
                const config = {
                    headers: {}
                };

                if (token) {
                    config.headers.Authorization = `Bearer ${token}`;
                }

                const response = await axios.get(
                    'https://localhost:8088/facilities/type?type=VETERINARY_CLINIC',
                    config
                );
                setFacilities(response.data);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        getFacilities();
    }, [token]);

    const generateTimeSlots = (openingTime, closingTime) => {
        const parseTime = (timeStr) => {
            const [time, modifier] = timeStr.split(' ');
            let [hours, minutes] = time.split(':');
            
            hours = parseInt(hours);
            minutes = parseInt(minutes);
            
            if (modifier === 'PM' && hours !== 12) {
                hours += 12;
            } else if (modifier === 'AM' && hours === 12) {
                hours = 0;
            }
            
            return { hours, minutes };
        };

        const opening = parseTime(openingTime);
        const closing = parseTime(closingTime);

        const openingDate = new Date();
        openingDate.setHours(opening.hours, opening.minutes, 0, 0);
        
        const closingDate = new Date();
        closingDate.setHours(closing.hours, closing.minutes, 0, 0);
        
        const diffInHours = (closingDate - openingDate) / (1000 * 60 * 60);
        const numberOfAppointments = Math.floor(diffInHours / appointmentDuration);
        
        const timeSlots = [];
        let currentTime = new Date(openingDate);
        
        for (let i = 0; i < numberOfAppointments; i++) {
            const slotStart = new Date(currentTime);
            const slotEnd = new Date(currentTime.getTime() + appointmentDuration * 60 * 60 * 1000);
            
            const formatTime = (date) => {
                let hours = date.getHours();
                const minutes = date.getMinutes();
                const ampm = hours >= 12 ? 'PM' : 'AM';
                hours = hours % 12;
                hours = hours ? hours : 12;
                const minutesStr = minutes < 10 ? '0' + minutes : minutes;
                return `${hours}:${minutesStr} ${ampm}`;
            };
            
            timeSlots.push({
                start: formatTime(slotStart),
                end: formatTime(slotEnd),
                value: `${formatTime(slotStart)} - ${formatTime(slotEnd)}`,
                dateTime: new Date(slotStart) // Store the actual Date object for comparison
            });
            
            currentTime = slotEnd;
        }
        
        return timeSlots;
    };

    const requestAppointment = async (facilityId, serviceProviderId, openingTime, closingTime) => {
if (!token) {
            // Store the current path before redirecting to login
            sessionStorage.setItem('redirectAfterLogin', location.pathname);

            Swal.fire({
                title: "Login Required",
                text: "You must be logged in to request an appointment",
                icon: "warning",
                showCancelButton: true,
                confirmButtonText: "Login",
                cancelButtonText: "Cancel"
            }).then((result) => {
                if (result.isConfirmed) {
                    navigate("/login", { 
                        state: { from: location.pathname } 
                    });
                }
            });
            return;
        }

        try {
            // Fetch existing appointments for this facility
            const appointmentsResponse = await axios.get(
                `https://localhost:8088/appointments/facility/${facilityId}`,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            const bookedAppointments = appointmentsResponse.data;
            const allTimeSlots = generateTimeSlots(openingTime, closingTime);

            // Get current date without time component
            const today = new Date();
            today.setHours(0, 0, 0, 0);

            // Filter out booked time slots
            const availableTimeSlots = allTimeSlots.filter(slot => {
                return !bookedAppointments.some(appointment => {
                    const appointmentDate = new Date(appointment.appointmentTime);
                    // Compare dates and times
                    return (
                        appointmentDate.getDate() === slot.dateTime.getDate() &&
                        appointmentDate.getMonth() === slot.dateTime.getMonth() &&
                        appointmentDate.getFullYear() === slot.dateTime.getFullYear() &&
                        appointmentDate.getHours() === slot.dateTime.getHours() &&
                        appointmentDate.getMinutes() === slot.dateTime.getMinutes()
                    );
                });
            });

            if (availableTimeSlots.length === 0) {
                Swal.fire("No Available Slots", "All appointment slots are booked for today.", "info");
                return;
            }

            Swal.fire({
                title: "Request Appointment",
                html: `
                    <input type="text" id="appointment-reason" class="swal2-input" placeholder="Appointment Reason" required>
                    <select id="appointment-time" class="swal2-input" required>
                        <option value="" disabled selected>Select a time slot</option>
                        ${availableTimeSlots.map(slot => 
                            `<option value="${slot.start}">${slot.value}</option>`
                        ).join('')}
                    </select>
                `,
                focusConfirm: false,
                showCancelButton: true,
                confirmButtonText: "Request",
                cancelButtonText: "Cancel",
                preConfirm: async () => {
                    const reason = document.getElementById('appointment-reason').value.trim();
                    const selectedTime = document.getElementById('appointment-time').value;

                    if (!reason || !selectedTime) {
                        Swal.showValidationMessage('Please fill all fields');
                        return false;
                    }

                    try {
                        const today = new Date();
                        const dateStr = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`;
                        const [hour, minAmpm] = selectedTime.split(':');
                        const [minutes, ampm] = minAmpm.split(' ');
                        const paddedTime = `${hour.padStart(2, '0')}:${minutes} ${ampm}`;
                        const requestedTime = `${dateStr} ${paddedTime}`;

                        await axios.post(
                            `https://localhost:8088/appointment-requests/add/user/${userId}`,
                            {
                                facilityId,
                                serviceProviderId,
                                requestReason: reason,
                                requestedTime,
                            },
                            {
                                headers: {
                                    Authorization: `Bearer ${token}`,
                                },
                            }
                        );
                        return true;
                    } catch (error) {
                        Swal.showValidationMessage(`Request failed: ${error.response?.data?.message || error.message}`);
                        return false;
                    }
                }
            }).then((result) => {
                if (result.isConfirmed) {
                    Swal.fire("Success", "Appointment requested successfully!", "success");
                }
            });

        } catch (error) {
            console.error("Error:", error);
            Swal.fire("Error", "Failed to process appointment request", "error");
        }
    };

    return (
        <div className="store-management-container">
            <h2 className="management-dashboard-title">Pet Clinics</h2>
            <div className="management-grid">
                {facilities.map((facility) => (
                    <div className="management-card" key={facility.facilityId}>
                        <img src={clinic} alt={facility.facilityName} />
                        <h4 className="management-card-title">{facility.facilityName}</h4>
                        <p>{facility.facilityDescription}</p>
                        <h5 className="price">{facility.facilityAddress}</h5>
                        <h5 className="price">Opening: {facility.openingTime}</h5>
                        <h5 className="price">Closing: {facility.closingTime}</h5>
                        <button
                            className="management-card-button"
                            onClick={() => requestAppointment(
                                facility.facilityId, 
                                facility.serviceProvider.serviceProviderId,
                                facility.openingTime,
                                facility.closingTime
                            )}
                        >
                            Request Appointment
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default ClinicAppointments;