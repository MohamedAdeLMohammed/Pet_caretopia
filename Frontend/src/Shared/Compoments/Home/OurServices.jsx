const servicesData = [
  {
    id: 1,
    title: "Pet Recognition",
    image: "../src/assets/recogination.jpg",
    description: "AI-Powered Breed Recognition. Upload your pet's photo and let our AI detect the breed instantly — fast, accurate, and helpful for new adopters",
    link: "/home/petRecogintion"
  },
  {
    id: 2,
    title: "Medical Care",
    image: "../src/assets/medical.jpg", 
    description: "Regular checkups and specialized veterinary treatments",
    link: "/home/petServices/clinics"
  },
  {
    id: 3,
    title: "Pet Adoption",
    image: "../src/assets/Adoption.png",
    description: "Adoption centers rescue and rehome animals",
    link: "/home/adoption"
  },
  {
    id: 4,
    title: "Behavior Training",
    image: "../src/assets/training.jpg",
    description: "Training programs to improve your pet's behavior",
    link: "/home/petServices/trainingCenter"
  },
];

function OurServices() {
  return (
    <section className="services-section" id="services-section">
      <h2 className="section-title">Our Services</h2>
      
      <div className="services-container">
        <div className="services-grid">
          {servicesData.map((service) => (
            <a 
              key={service.id} 
              href={service.link}
              className="service-card-link"
              style={{ textDecoration: 'none' }} // Added inline style
            >
              <div className="service-card">
                <div className="card-image">
                  <img 
                    src={service.image} 
                    alt={service.title} 
                    loading="lazy"
                  />
                </div>
                <div className="card-content">
                  <h3>{service.title}</h3>
                  <p>{service.description}</p>
                </div>
              </div>
            </a>
          ))}
        </div>
      </div>
    </section>
  );
}

export default OurServices;