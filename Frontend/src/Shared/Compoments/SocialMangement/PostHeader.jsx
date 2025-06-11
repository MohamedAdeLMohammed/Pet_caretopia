import { useState } from "react";
import { formatDistanceToNow } from "date-fns";
import profile from "../../../assets/profile image.png";
import { FaEllipsisV } from "react-icons/fa";
import PostMenu from "./PostMenu";

export default function PostHeader({ posts,post, userInfo, currentUserId, token }) {
  const [openMenuPostId, setOpenMenuPostId] = useState(null);

  const handleMenuToggle = (postId, e) => {
    e.stopPropagation();
    setOpenMenuPostId((prev) => (prev === postId ? null : postId));
  };

  return (
    <div className="post-head d-flex justify-content-between mb-3">
      <div className="d-flex gap-2 align-items-center">
        <img
          src={post.user.profileImageUrl || profile}
          alt="Profile"
          style={{ width: 60, height: 60, borderRadius: "50%" }}
        />
        <div>
          <h6 className="mb-0" style={{ textTransform: "upperCase", color: "#023C5A", fontSize: "20px" }}>
            {post.user.name}
          </h6>
          <small className="text-muted" style={{ fontSize: "15px" }}>
            {formatDistanceToNow(new Date(post.createdAt), { addSuffix: true })}
          </small>
        </div>
      </div>
      
      <div className="post-menu-container position-relative">
        <button
          className="btn p-0"
          onClick={(e) => handleMenuToggle(post.postId, e)}
        >
          <FaEllipsisV />
        </button>
        {openMenuPostId === post.postId && (
          <PostMenu 
            posts={posts} 
            post={post} 
            currentUserId={currentUserId} 
            token={token}
            onClose={() => setOpenMenuPostId(null)}
          />
        )}
      </div>
    </div>
  );
}