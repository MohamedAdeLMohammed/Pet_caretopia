import { FaThumbsUp, FaRegCommentDots, FaShare } from "react-icons/fa";
import Swal from "sweetalert2";
import axios from "axios";

export default function PostActions({
  post,
  onToggleComments,
  token,
  currentUserId,
  onReactionUpdate,
}) {
  const likeCount = post.reactions?.length || 0;
  const commentCount = post.comments?.length || 0;
  const shareCount = post.shares?.length || 0;

  const isLiked = post.reactions?.some(
    (r) => r.userId === currentUserId && r.type === "LIKE"
  );

  const postSharing = async (postId) => {
    const imageHtml = post.postImages?.length
      ? `<img src="${post.postImages[0].url}" style="max-width:100%; margin-top:10px;" />`
      : "";

    const { value: shareMessage } = await Swal.fire({
      title: "Share Post",
      html: `
        <textarea id="swal-post-content" rows="3" style="width:100%; padding:10px;" placeholder="Say something..."></textarea>
        <p><strong>Original Post:</strong> ${post.content}</p>
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

  const toggleReaction = async (postId) => {
    try {
      if (!isLiked) {
        const res = await axios.post(
          `https://localhost:8088/social/reactions/post/${postId}?type=LIKE`,
          {},
          { headers: { Authorization: `Bearer ${token}` } }
        );
        onReactionUpdate(postId, res.data); // Send new reaction
      } else {
        await axios.delete(
          `https://localhost:8088/social/reactions/post/${postId}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        onReactionUpdate(postId, null); // Remove reaction
      }
    } catch (error) {
      console.error("Reaction error:", error);
      Swal.fire("Error", "Failed to update reaction", "error");
    }
  };

  return (
    <div className="d-flex gap-3 align-items-center border-top pt-2">
      <button
        className="btn btn-light d-flex align-items-center gap-1"
        onClick={() => toggleReaction(post.postId)}
      >
        <FaThumbsUp className={isLiked ? "text-primary" : ""} />
        {likeCount}
      </button>

      <button
        className="btn btn-light d-flex align-items-center gap-1"
        onClick={() => onToggleComments(post.postId)}
      >
        <FaRegCommentDots style={{ fontSize: "20px" }} /> {commentCount}
      </button>

      <button
        className="btn btn-light d-flex align-items-center gap-1"
        onClick={() => postSharing(post.postId)}
      >
        <FaShare style={{ fontSize: "20px" }} /> {shareCount}
      </button>
    </div>
  );
}
