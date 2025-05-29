import { useEffect, useState } from "react";
import st1 from '/Ceratopia/Ceratopia/src/assets/st1.webp'
import st2 from '/Ceratopia/Ceratopia/src/assets/st2.webp'
import store from '/Ceratopia/Ceratopia/src/assets/store.jpg'

const images = [
  st1,st2,store

];

function StoreSlider() {
  const [current, setCurrent] = useState(0);

  useEffect(() => {
    const timer = setInterval(() => {
      setCurrent((prev) => (prev + 1) % images.length);
    }, 3000);
    return () => clearInterval(timer);
  }, []);

  return (
    <div className="slider-container">
      <img src={images[current]} alt="slide" className="slider-image" />
    </div>
  );
}

export default StoreSlider;