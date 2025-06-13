import { useState } from 'react';
import cat from '../../../assets/cat 1.png';
import Swal from 'sweetalert2';

function PetRecognition() {
  const [selectedImage, setSelectedImage] = useState(null);
  const [result, setResult] = useState(null);

  const handleFileChange = (e) => {
    setSelectedImage(e.target.files[0]);
  };

  const handleUpload = async () => {
    if (!selectedImage) {
      Swal.fire("Please select an image to upload.");
      return;
    }

    const formData = new FormData();
    formData.append('image', selectedImage);

    try {
      const res = await fetch('https://localhost:5000/predict', {
        method: 'POST',
        body: formData
      });

      if (!res.ok) {
        throw new Error('Upload failed');
      }

      const data = await res.json();
      setResult(data);

      Swal.fire({
        title: 'Prediction Complete',
        html: `
          <strong>Breed:</strong> ${data.breed}<br/>
          <strong>Confidence:</strong> ${(data.confidence * 100).toFixed(2)}%
        `,
        icon: 'success'
      });

    } catch (err) {
      Swal.fire("Error", err.message, "error");
    }
  };

  return (
    <div className="container d-flex flex-column align-items-center gap-3 pt-3">

      <h2 className='section-title'style={{fontWeight:"500px"}}>Pet Recoginition</h2>

      <div className="d-flex flex-column align-items-center gap-3">
        <h2 style={{fontSize:"30px"}}>Upload a photo of your pet, and let our AI do the magic!</h2>

        {/* File input */}
        <input type="file" accept="image/*" onChange={handleFileChange} className="form-control mb-2" />

        {/* Upload button now triggers upload to Flask API */}
        <button className="Add-btn" onClick={handleUpload}>
          <h4>Submit Photo</h4>
        </button>
      </div>

      <div className="photo-section d-flex flex-column align-items-center gap-3 mb-2">
        <h5 style={{fontSize:"20px"}}>
          Ensure your pet’s face and body are clearly visible in the image for accurate analysis.
        </h5>
        <div className='d-flex flex-column align-items-center'>
          <img style={{width:"350px",height:"350px"}} src={selectedImage ? URL.createObjectURL(selectedImage) : cat} className='pet-reco-photo' alt="Pet Preview" />
        </div>
      </div>

      {result && (
        <div className='result-section d-flex flex-column align-items-center gap-2 mt-2'>
          <h2 className='blue-text fw-bolder'>Result:</h2>
          <h2>Breed Identification: {result.breed}</h2>
          <h2>Confidence: {(result.confidence * 100).toFixed(2)}%</h2>
        </div>
      )}
    </div>
  );
}

export default PetRecognition;
