import Banner from '/src/assets/Banner.jpg';

function HomeBanner() {
    const scrollToServices = () => {
    const section = document.getElementById('services-section');
    if (section) {
      section.scrollIntoView({ behavior: 'smooth' });
    }
  };
  return (
    <div className="banner-container">
      <div className="banner-overlay"></div>
      <div className="banner-content">
        <h1 className="welcome-text">Welcome to <span style={{color:"#ff3399"}}>Pet CareTopia</span></h1>
        <h3 className="sub-text">Where Your Pet’s Happiness Comes First!</h3>
        <button className="banner-button" onClick={scrollToServices}>
          Explore Services 
          <span className="arrow">→</span>
        </button>
      </div>
    </div>
  );
}

export default HomeBanner;