import cat from '/Ceratopia/Ceratopia/src/assets/cat 1.png'
function AdoptationOffers(){
    return(
        <>
        <div className="container d-flex gap-3 p-3">
            <div className="pet-card d-flex flex-column">
                <img src={cat} alt="" srcset="" />
                <p>Cat 2</p>
                <button className='adopt-btn'>Request To Adopt</button>
            </div>
            <div className="pet-card d-flex flex-column">
                <img src={cat} alt="" srcset="" />
                <p>Cat 2</p>
                <button className='adopt-btn'>Request To Adopt</button>
            </div>
        </div>
        </>
    );
}
export default AdoptationOffers;