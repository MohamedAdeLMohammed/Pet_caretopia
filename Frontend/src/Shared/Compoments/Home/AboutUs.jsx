import { FaTrophy, FaHeart, FaUsers, FaPaw } from 'react-icons/fa';

function AboutUs(){
  return (
    <section className="about-section">
      <div className="about-container">
        <div className="about-content">
          <h2 className="about-section-title">Our Story</h2>
          <p className="about-text">
            Founded in 2015, Pet Caretopia began as a small clinic with big dreams. 
            Today, we're Cairo's leading pet care provider, offering comprehensive 
            services with love and expertise.
          </p>
          
          <div className="achievements-grid">
            <div className="achievement-card">
              <FaTrophy className="achievement-icon" />
              <h3>12+</h3>
              <p>Industry Awards</p>
            </div>
            
            <div className="achievement-card">
              <FaUsers className="achievement-icon" />
              <h3>5,000+</h3>
              <p>Happy Clients</p>
            </div>
            
            <div className="achievement-card">
              <FaHeart className="achievement-icon" />
              <h3>2,000+</h3>
              <p>Successful Adoptions</p>
            </div>
            
            <div className="achievement-card">
              <FaPaw className="achievement-icon" />
              <h3>50+</h3>
              <p>Professional Staff</p>
            </div>
          </div>
        </div>
        
        <div className="about-image">
          <img src="../src/assets/team.jpg" alt="Our Team" />
        </div>
      </div>
    </section>
  );
};

export default AboutUs;