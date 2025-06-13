import { FaFacebook, FaInstagram, FaTwitter, FaMapMarkerAlt, FaPhone, FaEnvelope } from 'react-icons/fa';

function Footer(){
  return (
    <footer className="site-footer">
      <div className="footer-container">
        
        <div className="footer-section">
          <h3 className="footer-logo">Pet Caretopia</h3>
          <p className="footer-about">
            Your trusted partner in premium pet care services since 2015.
          </p>
          <div className="footer-social-icons">
            <a href="#"><FaFacebook className="icon" /></a>
            <a href="#"><FaInstagram className="icon" /></a>
            <a href="#"><FaTwitter className="icon" /></a>
          </div>
        </div>

        <div className="footer-section">
          <h4>Quick Links</h4>
          <ul className="footer-links">
            <li><a href="/">Home</a></li>
            <li><a href="/services">Services</a></li>
            <li><a href="/about">Adoption</a></li>
            <li><a href="/blog">Community</a></li>
            <li><a href="/contact">Store</a></li>
          </ul>
        </div>

        <div className="footer-section contact-section">
          <h4>Contact Us</h4>
          <div className="footer-contact-info">
            <div className="footer-contact-item">
              <FaMapMarkerAlt className="footer-contact-icon" />
              <span>123 Pet Street, Nasr City, Cairo, Egypt</span>
            </div>
            <div className="footer-contact-item">
              <FaPhone className="footer-contact-icon" />
              <span>+02 249 71511</span>
            </div>
            <div className="footer-contact-item">
              <FaEnvelope className="footer-contact-icon" />
              <span>info@petcaretopia.com</span>
            </div>
          </div>
        </div>
      </div>

      <div className="copyright">
        <p>&copy; {new Date().getFullYear()} Pet Caretopia. All Rights Reserved.</p>
      </div>
    </footer>
  );
};

export default Footer;