import { useState } from 'react';
import { FaChevronDown, FaChevronUp, FaPaw } from 'react-icons/fa';

function FAQSection(){
  const [activeIndex, setActiveIndex] = useState(null);

  const toggleFAQ = (index) => {
    setActiveIndex(activeIndex === index ? null : index);
  };

  const faqs = [
    {
      question: "What vaccinations does my pet need?",
      answer: "Core vaccines include rabies, distemper, parvovirus, and adenovirus for dogs, and panleukopenia, calicivirus, and herpesvirus for cats. We'll create a personalized schedule during your first visit."
    },
    {
      question: "How often should I bring my pet for check-ups?",
      answer: "Adult pets should have annual wellness exams, while seniors (7+ years) need bi-annual visits. Puppies/kittens require more frequent visits during their first year."
    },
    {
      question: "Do you offer emergency services?",
      answer: "Yes, our 24/7 emergency clinic handles urgent cases. Call (02) 249-71511 immediately for assistance."
    },
    {
      question: "What payment methods do you accept?",
      answer: "We accept cash, credit cards, pet insurance, and offer flexible payment plans for major procedures."
    },
    {
      question: "How can I prepare my pet for their first visit?",
      answer: "Bring any medical records, a list of current medications, and your pet's favorite treats. Keep dogs leashed and cats in carriers for safety."
    }
  ];

  return (
    <section className="faq-section">
      <div className="faq-header">
        <FaPaw className="paw-icon" />
        <h2>Frequently Asked Questions</h2>
        <p>Find answers to common pet care queries</p>
      </div>
      
      <div className="faq-container">
        {faqs.map((faq, index) => (
          <div 
            key={index} 
            className={`faq-item ${activeIndex === index ? 'active' : ''}`}
            onClick={() => toggleFAQ(index)}
          >
            <div className="faq-question">
              <h3>{faq.question}</h3>
              {activeIndex === index ? <FaChevronUp /> : <FaChevronDown />}
            </div>
            <div className="faq-answer">
              <p>{faq.answer}</p>
            </div>
          </div>
        ))}
      </div>
    </section>
  );
};

export default FAQSection;