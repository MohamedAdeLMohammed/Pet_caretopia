import { useState } from "react";
import cat from '/Ceratopia/Ceratopia/src/assets/cat 1.png'
import axios from 'axios';
function PetsMangement(){
    const [pets,setPets] = useState([]);
    const getOwnerPets = async () => {
        try {
            const response = await axios.get('https://fakestoreapi.com/products/1');
            console.log(response.data);  // This logs the product details
            // You can set state here, for example:
            // setPets(response.data.vaccines.$values)
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };
    getOwnerPets();    
    return(
        <>
        <div className=" container row gap-5 pt-3 pb-3 ms-2 pets-cards">
        <div className="card" style={{ width: '18rem' }}>
      <img src={cat} className="card-img-top" alt="..." />
      <div className="card-body">
        <h5 className="card-title">Card title</h5>
        <p className="card-text">
          Some quick example text to build on the card title and make up the bulk of the card's content.
        </p>
        <a href="#" className="btn btn-primary">Go somewhere</a>
      </div>
    </div>
    <div className="card" style={{ width: '18rem' }}>
      <img src={cat} className="card-img-top" alt="..." />
      <div className="card-body">
        <h5 className="card-title">Card title</h5>
        <p className="card-text">
          Some quick example text to build on the card title and make up the bulk of the card's content.
        </p>
        <a href="#" className="btn btn-primary">Go somewhere</a>
      </div>
    </div>
    <div className="card" style={{ width: '18rem' }}>
      <img src={cat} className="card-img-top" alt="..." />
      <div className="card-body">
        <h5 className="card-title">Card title</h5>
        <p className="card-text">
          Some quick example text to build on the card title and make up the bulk of the card's content.
        </p>
        <a href="#" className="btn btn-primary">Go somewhere</a>
      </div>
    </div>
    <div className="card" style={{ width: '18rem' }}>
      <img src={cat} className="card-img-top" alt="..." />
      <div className="card-body">
        <h5 className="card-title">Card title</h5>
        <p className="card-text">
          Some quick example text to build on the card title and make up the bulk of the card's content.
        </p>
        <a href="#" className="btn btn-primary">Go somewhere</a>
      </div>
    </div>
    <div className="card" style={{ width: '18rem' }}>
      <img src={cat} className="card-img-top" alt="..." />
      <div className="card-body">
        <h5 className="card-title">Card title</h5>
        <p className="card-text">
          Some quick example text to build on the card title and make up the bulk of the card's content.
        </p>
        <a href="#" className="btn btn-primary">Go somewhere</a>
      </div>
    </div>
    <div className="card" style={{ width: '18rem' }}>
      <img src={cat} className="card-img-top" alt="..." />
      <div className="card-body">
        <h5 className="card-title">Card title</h5>
        <p className="card-text">
          Some quick example text to build on the card title and make up the bulk of the card's content.
        </p>
        <a href="#" className="btn btn-primary">Go somewhere</a>
      </div>
    </div>
        </div>
        </>
    );
}
export default PetsMangement;