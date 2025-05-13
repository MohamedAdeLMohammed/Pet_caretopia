import profile from '/Ceratopia/Ceratopia/src/assets/profile image.png'
import cat from '/Ceratopia/Ceratopia/src/assets/cat 1.png'
import like from '/Ceratopia/Ceratopia/src/assets/like.png'
import comment from '/Ceratopia/Ceratopia/src/assets/comment.png'
function Community(){
    return(
        <>
        <div className='container p-3 d-flex flex-column justify-content-center align-items-center gap-3'>
            <div className="add-post-section">
                <h4>what’s on your mind ?!</h4>
            </div>
            <div className="posts-section d-flex flex-column gap-2">
                <div className="post d-flex flex-column gap-1">
                    <div className="post-head d-flex align-items-center">
                        <img src={profile} alt="" />
                        <h6>Rihana</h6>
                    </div>
                    <p>This Cat is Missing</p>
                    <img src={cat} alt=""  />
                    <div className='post-foot d-flex align-items-center justify-content-between'>
                        <img src={like} alt="" className='' />
                        <img src={comment} alt="" />
                    </div>
                </div>
                <div className="post d-flex flex-column">
                    <div className="post-head d-flex align-items-center">
                        <img src={profile} alt="" />
                        <h6>Rihana</h6>
                    </div>
                    <p>This Cat is Missing</p>
                    <img src={cat} alt="" />
                    <div className='post-foot d-flex align-items-center justify-content-between'>
                        <img src={like} alt="" />
                        <img src={comment} alt="" />
                    </div>
                </div>
                <div className="post d-flex flex-column">
                    <div className="post-head d-flex align-items-center">
                        <img src={profile} alt="" />
                        <h6>Rihana</h6>
                    </div>
                    <p>This Cat is Missing</p>
                    <img src={cat} alt="" />
                    <div className='post-foot d-flex align-items-center justify-content-between'>
                        <img src={like} alt="" />
                        <img src={comment} alt="" />
                    </div>
                </div>
            </div>
        </div>
        </>
    );
}
export default Community;