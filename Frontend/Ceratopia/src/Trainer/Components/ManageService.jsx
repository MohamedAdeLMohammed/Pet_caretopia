import "../CSS/TrainerDashboard.css";
function ManageService() {
  return (
    <div className="vet-dashboard">
      <main className="content">
        <div className="header">
          <h2>Add Service as a Pet Trainer Or Sitter</h2>
          <p>Start connecting with pet owners by adding your service now !</p>
        </div>

        <form className="vet-form">
          <div className="form-group">
            <label>Full Name</label>
            <input type="text" placeholder="eg: John Doe" />
          </div>

          <div className="form-group">
            <label>Service Type</label>
            <select>
              <option>Trainning</option>
              <option>Sitting</option>
            </select>
          </div>

          <div className="form-group">
            <label>Service Description </label>
            <input type="text" placeholder="Description" />
          </div>

          <div className="form-group">
            <label>Email</label>
            <input type="email" placeholder="eg: john@email.com" />
          </div>

          <div className="form-group">
            <label>Service Price</label>
            <input type="text" placeholder="$ 500" />
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


          <button className="submit-btn">
            Submit <span>↪️</span>
          </button>
        </form>
      </main>
    </div>
  );
}

export default ManageService;
