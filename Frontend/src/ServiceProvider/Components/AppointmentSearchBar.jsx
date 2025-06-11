import { useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";

function AppointmentSearchBar({ setAppointments, token }) {
  const [searchConcept, setSearchConcept] = useState("ALL");
  const [keyword, setKeyword] = useState("");
  const { facilityId } = useParams();
  const handleCategorySelect = async (category) => {
    setSearchConcept(category);

    try {
      const url =
        category === "ALL"
          ? `https://localhost:8088/appointment-requests/facility/${facilityId}
`
          : `https://localhost:8088/appointment-requests/status?status=${category}`;
      
      const res = await axios.get(url, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      setAppointments(res.data);
    } catch (err) {
      console.error("Category fetch error:", err);
    }
  };

  const handleSearch = async () => {
    if (!keyword.trim()) return;
    try {
      const res = await axios.get(
        `https://localhost:8088/facilities/facilityName?=${keyword}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      setFacilities(res.data);
    } catch (err) {
      console.error("Search error:", err);
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter") handleSearch();
  };

  return (
    <div className="input-group mb-3">
      <div className="input-group-prepend">
        <button
          className="btn btn-outline-secondary dropdown-toggle"
          type="button"
          data-bs-toggle="dropdown"
        >
          {searchConcept}
        </button>
        <ul className="dropdown-menu">
          {["ALL", "PENDING", "ACCEPTED", "REJECTED" ,"CANCELED"].map(
            (category) => (
              <li key={category}>
                <button
                  className="dropdown-item"
                  onClick={() => handleCategorySelect(category)}
                >
                  {category}
                </button>
              </li>
            )
          )}
        </ul>
      </div>
      <input
        type="text"
        className="form-control"
        placeholder="Search"
        value={keyword}
        onChange={(e) => setKeyword(e.target.value)}
        onKeyDown={handleKeyDown}
      />
      <button className="btn btn-outline-primary" onClick={handleSearch}>
        🔍
      </button>
    </div>
  );
}

export default AppointmentSearchBar;
