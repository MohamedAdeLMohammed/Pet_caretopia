import "../CSS/VetDashboard.css";
function ManageAccounts() {
  return (
    <div className="vet-dashboard">
      <main className="content">
        <div className="header">
          <h2>Create or Update Veterinary Account</h2>
          <p>Manage your professional profile as a veterinarian</p>
        </div>

        <form className="vet-form">
          <div className="form-group">
            <label>Full Name</label>
            <input type="text" placeholder="eg: John Doe" />
          </div>

          <div className="form-group">
            <label>Specialization</label>
            <select>
              <option>Surgery</option>
              <option>Dermatology</option>
              <option>Nutrition</option>
            </select>
          </div>

          <div className="form-group">
            <label>Location</label>
            <input type="text" placeholder="Location" />
          </div>

          <div className="form-group">
            <label>Clinic Name</label>
            <input type="text" placeholder="eg: john@email.com" />
          </div>

          <div className="form-group">
            <label>License Number</label>
            <input type="text" placeholder="543210987" />
          </div>

          <div className="form-group">
            <label>Years of Experience</label>
            <input type="number" placeholder="0" />
          </div>

          <div className="form-group">
            <label>Phone Number</label>
            <div className="phone-field">
              <span>+022</span>
              <input type="text" placeholder="123456789" />
            </div>
          </div>

          <div className="form-group">
            <label>Working Hours</label>
            <input type="text" placeholder="e.g. 9am - 5pm" />
          </div>

          <button className="submit-btn">
            Submit <span>↪️</span>
          </button>
        </form>
      </main>
    </div>
  );
}

export default ManageAccounts;
