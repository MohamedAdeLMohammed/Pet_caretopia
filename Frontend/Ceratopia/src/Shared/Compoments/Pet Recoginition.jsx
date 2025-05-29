import cat from '/Ceratopia/Ceratopia/src/assets/cat 1.png'
import Swal from 'sweetalert2'
function PetRecoginition(){
    const addPetBtn = ()=>{
        Swal.fire({
            title: "Add Pet",
            html: `
              <div class="mb-3">
                <label for="formFile" class="form-label">Pet Name :</label>
                <input class="form-control" type="text" id="formFile" />
              </div>
              <div class="mb-3">
                <label for="formFileMultiple" class="form-label">Pet Age :</label>
                <input class="form-control" type="text" id="formFileMultiple" />
              </div>
              <div class="mb-3">
                <label for="formFileDisabled" class="form-label">Breed Identification :</label>
                <input class="form-control" type="text" id="formFileDisabled" />
              </div>
            `,
            showCancelButton: true,
            confirmButtonText: "Submit",
            preConfirm: () => {
              const file = document.getElementById("formFile").files[0];
              if (!file) {
                Swal.showValidationMessage("Please upload a file!");
              }
              return file;
            }
          }).then((result) => {
            if (result.isConfirmed) {
              console.log("File uploaded:", result.value);
              // You can handle the file here
            }
          });
    }
    return(
        <>
        <div className="container d-flex flex-column align-items-center gap-3 pt-3">
            <h2 className='blue-text fw-bolder'>Discover Your Pet's Breed in Seconds!</h2>
            <div className="d-flex flex-column align-items-center gap-3">
            <h2 className='fw-bolder'>Upload a photo of your pet, and let our AI do the magic!</h2>
            <button className="upload-btn">
                <h4>Upload Photo</h4>
            </button>
            </div>
            <div className="photo-section d-flex flex-column align-items-center gap-3">
            <h5 className='fw-bolder'>Ensure your pet’s face and body are clearly visible in the image for accurate analysis.</h5>
            <div className='d-flex flex-column align-items-center'>
            <img src={cat} className='pet-reco-photo' alt="" />
            <button className='w-100 analyze-btn'>
                <h5>Analyze Photo</h5>
                </button>
            </div>
            </div>
            <div className='result-section d-flex flex-column align-items-center gap-2 mt-2'>
                <h2 className='blue-text fw-bolder'>Result:</h2>
                <h2>Breed Identification: American Shorthair</h2>
                <h2>Approximate Age: Kitten(1:3 months) </h2>
            </div>
            <div className='d-flex flex-column align-items-end justify-content-end'>
                <button onClick={addPetBtn} className='upload-btn'>
                    <h4>Add Pet</h4>
                    </button>
            </div>
        </div>
        </>
    );
}
export default PetRecoginition;