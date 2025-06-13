import "../CSS/SocialCommunity.css";
import { useState, useEffect, useRef } from "react";
import { formatDistanceToNow } from "date-fns";
import axios from "axios";
import Swal from "sweetalert2";
import { jwtDecode } from "jwt-decode";
import profile from "../../../assets/profile image.png";
import {
  FaThumbsUp,
  FaRegCommentDots,
  FaShare,
  FaEllipsisV,
} from "react-icons/fa";

function Posts() {
  const [posts, setPosts] = useState([]);
  const [showReactions, setShowReactions] = useState(false);
  const [openCommentPostId, setOpenCommentPostId] = useState(null);
  const [openMenuPostId, setOpenMenuPostId] = useState(null);
  const [comments, setComments] = useState({});
  const [newComment, setNewComment] = useState("");
  const [selectedImages, setSelectedImages] = useState([]);
  const [userReactions, setUserReactions] = useState({});
  const hideTimerRef = useRef(null);
  const token = sessionStorage.getItem("token");
  const decode = jwtDecode(token);

  useEffect(() => {
    const getPosts = async () => {
      try {
        const response = await axios.get(`https://localhost:8088/social/posts`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setPosts(response.data);
        console.log(response.data)
        const reactionsMap = {};
response.data.forEach((post) => {
  const reacted = post.reactions?.find((r) => r.userId === decode.id);
  if (reacted) reactionsMap[post.postId] = reacted.type;
});

        setUserReactions(reactionsMap);
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to fetch posts", "error");
      }
    };
    getPosts();
  }, [token]);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (!event.target.closest(".post-menu-container")) {
        setOpenMenuPostId(null);
      }
    };
    document.addEventListener("click", handleClickOutside);
    return () => document.removeEventListener("click", handleClickOutside);
  }, []);

const handleReaction = async (postId, type) => {
  try {
    const res = await axios.post(
      `https://localhost:8088/social/reactions/post/${postId}?type=${type}`,
      {},
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    const updatedPosts = posts.map((post) => {
      if (post.postId === postId) {
        // Remove old reaction from the same user if it exists
        const filteredReactions = (post.reactions || []).filter(
          (r) => r.userId !== decode.id
        );

        return {
          ...post,
          reactions: [...filteredReactions, res.data], // Add new reaction
        };
      }
      return post;
    });

    setPosts(updatedPosts);
  } catch (error) {
    console.error("Failed to send reaction:", error);
  }
};




const handleToggleComments = (postId) => {
  setOpenCommentPostId((prev) => (prev === postId ? null : postId));
};


  const handleMenuToggle = (postId) => {
    setOpenMenuPostId((prev) => (prev === postId ? null : postId));
  };

  const handleDelete = (postId) => {
    Swal.fire({
      title: "Are you sure?",
      text: "Do you want to delete this post?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Yes, delete it!",
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          await axios.delete(`https://localhost:8088/social/posts/${postId}`, {
            headers: { Authorization: `Bearer ${token}` },
          });
          setPosts(posts.filter((post) => post.postId !== postId));
          Swal.fire("Deleted!", "Your post has been deleted.", "success");
        } catch (error) {
          console.error("Delete failed:", error);
          Swal.fire("Error", "Failed to delete post", "error");
        }
      }
    });
  };

  const handleEdit = (postId) => {
    Swal.fire("Edit", `You clicked edit on post ${postId}`, "info");
  };

  const handleReport = async (postId) => {
    Swal.fire("Report", `You reported post ${postId}`, "warning");

    try {
      await axios.post(`https://localhost:8088/social/reports`, {
        reportedPostId: postId,
        reportReason: "Inappropriate content in post",
      }, {
        headers: { Authorization: `Bearer ${token}` },
      });
      Swal.fire("Reported!", "Your post has been reported.", "success");
    } catch (error) {
      console.error("Report failed:", error);
      Swal.fire("Error", "Failed to report post", "error");
    }
  };

  const postSharing = async (postId) => {
    const targetPost = posts.find((p) => p.postId === postId);
    if (!targetPost) return Swal.fire("Error", "Post not found", "error");

    const imageHtml = targetPost.postImages?.length
      ? `<img src="${targetPost.postImages[0].url}" style="max-width:100%; margin-top:10px;" />`
      : "";

    const { value: shareMessage } = await Swal.fire({
      title: "Share Post",
      html: `
        <textarea id="swal-post-content" rows="3" style="width:100%; padding:10px;" placeholder="Say something..."></textarea>
        <p><strong>Original Post:</strong> ${targetPost.content}</p>
        ${imageHtml}
      `,
      showCancelButton: true,
      confirmButtonText: "Share",
      preConfirm: () => document.getElementById("swal-post-content").value,
    });

    if (shareMessage !== undefined) {
      try {
        await axios.post(
          `https://localhost:8088/social/shares`,
          { postId, shareMessage: shareMessage || null },
          { headers: { Authorization: `Bearer ${token}` } }
        );
        Swal.fire("Shared!", "Post has been shared successfully.", "success");
      } catch (error) {
        Swal.fire("Error", "Failed to share post", "error");
      }
    }
  };

  const handleFileChange = (e) => {
    setSelectedImages(Array.from(e.target.files));
  };

  const submitComment = async (postId) => {
    if (!newComment.trim()) return;
    try {
      const formData = new FormData();
      const commentObj = { content: newComment, postId };
      formData.append("comment", new Blob([JSON.stringify(commentObj)], { type: "application/json" }));
      selectedImages.forEach((img) => formData.append("images", img));

      await axios.post(`https://localhost:8088/social/comments/add`, formData, {
        headers: { Authorization: `Bearer ${token}` },
      });

      setNewComment("");
      setSelectedImages([]);
    } catch (error) {
      Swal.fire("Error", "Failed to submit comment", "error");
    }
  };

  return (
    <div className="posts-section d-flex flex-column gap-2">
      {posts.map((post) => {
        const likeCount = post.reactions?.length || 0;
        const commentCount = post.comments.length || 0;
        const shareCount = post.shares.length || 0;
        const isLiked = userReactions[post.postId] === "LIKE";

        return (
          <div key={post.postId} className="post p-3 bg-white border rounded shadow-sm">
            <div className="post-head d-flex justify-content-between">
              <div className="d-flex gap-2 align-items-center">
                <img src={profile} alt="Profile" style={{ width: 40, height: 40, borderRadius: "50%" }} />
                <div>
                  <h6 className="mb-0">{post.user.name}</h6>
                  <small className="text-muted">{formatDistanceToNow(new Date(post.createdAt), { addSuffix: true })}</small>
                </div>
              </div>
              <div className="post-menu-container position-relative">
                <button className="btn p-0" onClick={(e) => { e.stopPropagation(); handleMenuToggle(post.postId); }}>
                  <FaEllipsisV />
                </button>
                {openMenuPostId === post.postId && (
                  <div className="menu position-absolute bg-white border rounded shadow p-2">
                    {post.user.userID === decode.id ? (
                      <>
                        <button className="dropdown-item" onClick={() => handleEdit(post.postId)}>Edit</button>
                        <button className="dropdown-item" onClick={() => handleDelete(post.postId)}>Delete</button>
                      </>
                    ) : (
                      <button className="dropdown-item" onClick={() => handleReport(post.postId)}>Report</button>
                    )}
                  </div>
                )}
              </div>
            </div>

            <p>{post.content}</p>
            {post.postImages?.length > 0 && (
              <img src={post.postImages[0].url} alt="Post" className="img-fluid rounded" />
            )}

            <div className="d-flex gap-3 mt-2 align-items-center">
              <button
                className={`btn btn-light d-flex align-items-center gap-1 ${isLiked ? "text-primary" : ""}`}
                onClick={() => handleReaction(post.postId, "LIKE")}
              >
                <FaThumbsUp /> {likeCount}
              </button>

              <button className="btn btn-light d-flex align-items-center gap-1" onClick={() => handleToggleComments(post.postId)}>
                <FaRegCommentDots /> {commentCount}
              </button>

              <button className="btn btn-light d-flex align-items-center gap-1" onClick={() => postSharing(post.postId)}>
                <FaShare /> {shareCount}
              </button>
            </div>

            {openCommentPostId === post.postId && (
              <div className="comment-section mt-3">
                <input
                  type="text"
                  className="form-control"
                  placeholder="Write a comment..."
                  value={newComment}
                  onChange={(e) => setNewComment(e.target.value)}
                />
                <input type="file" multiple onChange={handleFileChange} className="form-control mt-1" />
                <button className="btn btn-primary mt-2" onClick={() => submitComment(post.postId)}>Submit</button>

                <div className="comments-list mt-2">
                  {post.comments?.length > 0 ? (
  post.comments.map((comment) => (
    <div key={comment.commentId} className="comment-box p-2 mb-2 border rounded bg-light">
      <div className="d-flex justify-content-between">
        <span className="fw-bold">{comment.user?.name || "User"}</span>
        <small className="text-muted">{formatDistanceToNow(new Date(comment.createdAt), { addSuffix: true })}</small>
      </div>
      <p>{comment.content}</p>
      {comment.commentImages?.length > 0 && (
        <img src={comment.commentImages[0].url} alt="comment" className="img-fluid rounded" style={{ maxWidth: "150px" }} />
      )}
    </div>
  ))
) : (
  <p className="text-muted">No comments yet.</p>
)}

                </div>
              </div>
            )}
          </div>
        );
      })}
    </div>
  );
}

export default Posts;
