const servicesData = [
  {
    id: 1,
    title: "Grooming & Cleaning",
    image: "../src/assets/grooming.jpg",
    description: "Professional grooming services to make your pet look fabulous"
  },
  {
    id: 2,
    title: "Medical Care",
    image: "../src/assets/medical.jpg", 
    description: "Regular checkups and specialized veterinary treatments"
  },
  {
    id: 3,
    title: "Pet Adoption",
    image: "../src/assets/Adoption.png",
    description: "Adoption centers rescue and rehome animals"
  },
  {
    id: 4,
    title: "Behavior Training",
    image: "../src/assets/training.jpg",
    description: "Training programs to improve your pet's behavior"
  },
];

function OurServices() {
  return (
    <section className="services-section">
      <h2 className="section-title">Our Services</h2>
      
      <div className="services-container">
        <div className="services-grid">
          {servicesData.map((service) => (
            <div key={service.id} className="service-card">
              <div className="card-image">
                <img src={service.image} alt={service.title} />
              </div>
              <div className="card-content">
                <h3>{service.title}</h3>
                <p>{service.description}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

export default OurServices;