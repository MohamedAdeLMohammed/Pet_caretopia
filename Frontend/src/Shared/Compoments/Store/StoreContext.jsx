import { createContext, useState, useEffect } from "react";
import axios from "axios";

export const StoreContext = createContext();

export const StoreProvider = ({ children }) => {
  const token = sessionStorage.getItem("token");
  const [cartCount, setCartCount] = useState(0);
  const [wishCount, setWishCount] = useState(0);

  const fetchCartCount = async () => {
    if (!token) return;
    try {
      const res = await axios.get(`https://localhost:8088/cart/count`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setCartCount(res.data); // or res.data.count if response is { count: 5 }
    } catch (err) {
      console.error("Failed to fetch cart count:", err);
    }
  };

  const fetchWishCount = async () => {
    if (!token) return;
    try {
      const res = await axios.get(`https://localhost:8088/wishlist/count`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setWishCount(res.data); // or res.data.count
    } catch (err) {
      console.error("Failed to fetch wishlist count:", err);
    }
  };

  useEffect(() => {
    if (token) {
      fetchCartCount();
      fetchWishCount();
    }
  }, [token]);

  return (
    <StoreContext.Provider
      value={{
        cartCount,
        wishCount,
        refreshCartCount: fetchCartCount,
        refreshWishCount: fetchWishCount,
      }}
    >
      {children}
    </StoreContext.Provider>
  );
};

