import { useState } from "react";
import axios from "axios";
import CommentItem from "./CommentItem";

export default function CommentSection({ post, token }) {
  const [newComment, setNewComment] = useState("");
  const [selectedImages, setSelectedImages] = useState([]);

  const handleFileChange = (e) => {
    setSelectedImages(Array.from(e.target.files));
  };

  const submitComment = async (postId) => {
    if (!newComment.trim()) return;
    try {
      const formData = new FormData();
      const commentObj = { content: newComment, postId };
      formData.append(
        "comment",
        new Blob([JSON.stringify(commentObj)], { type: "application/json" })
      );
      selectedImages.forEach((img) => formData.append("images", img));

      await axios.post(`https://localhost:8088/social/comments/add`, formData, {
        headers: { Authorization: `Bearer ${token}` },
      });

      setNewComment("");
      setSelectedImages([]);
    } catch (error) {
      console.error("Failed to submit comment:", error);
    }
  };

  return (
    <div className="comment-section mt-3">
      <div className="input-group mb-2">
        <input
          type="text"
          className="form-control"
          placeholder="Write a comment..."
          value={newComment}
          onChange={(e) => setNewComment(e.target.value)}
        />
        <button
          className="btn btn-primary"
          style={{ backgroundColor: "#ff3399", borderColor: "#ff3399" }}
          onClick={() => submitComment(post.postId)}
        >
          Post
        </button>
      </div>
      <input
        type="file"
        multiple
        onChange={handleFileChange}
        className="form-control mb-3"
      />

      <div className="comments-list">
        {post.comments?.length > 0 ? (
          post.comments.map((comment) => (
            <CommentItem key={comment.commentId} comment={comment} />
          ))
        ) : (
          <p className="text-muted">No comments yet</p>
        )}
      </div>
    </div>
  );
}