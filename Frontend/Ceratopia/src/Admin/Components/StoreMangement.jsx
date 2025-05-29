import { Link } from "react-router-dom";

function StoreMangement(){
    return(
        <>
        <div className="all-cats-container">
        <h2>Store Mangement</h2>

        <div className="product-grid">
            <div className="product-card">
              <img src={''} alt={''} />
              <h4 className="ProductName">Orders</h4>
              {/* <p>{product.description}</p>
              <h5 className="price">{product.price}</h5> */}
            <Link to={'orders'} className="add-to-cart">Mange Products</Link>
            </div>
            <div className="product-card">
              <img src={''} alt={''} />
              <h4 className="ProductName">Products</h4>
              {/* <p>{product.description}</p>
              <h5 className="price">{product.price}</h5> */}

              <Link to={'products'} className="add-to-cart">Mange Products</Link>
            </div>
            
        </div>
      </div>
        </>
    );
}
export default StoreMangement;