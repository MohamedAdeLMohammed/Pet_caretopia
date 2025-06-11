import { useState } from "react";
import { formatDistanceToNow } from "date-fns";
import PostHeader from "./PostHeader";
import PostActions from "./PostActions";
import CommentSection from "./CommentSection";

export default function PostItem({ posts,post, userInfo, currentUserId, token, onReactionUpdate }) {
  const [openCommentPostId, setOpenCommentPostId] = useState(null);

  const handleToggleComments = (postId) => {
    setOpenCommentPostId((prev) => (prev === postId ? null : postId));
  };

  return (
    <div className="post card">
      <div className="card-body">
        <PostHeader 
          posts={posts} 
          post={post} 
          userInfo={userInfo} 
          currentUserId={currentUserId} 
          token={token}
        />

        <p className="card-text mb-3" style={{ fontSize: "1.1rem" }}>
          {post.content}
        </p>

        {post.postImages?.length > 0 && (
          <div className="post-image-container mb-3">
            <img
              src={post.postImages[0].url}
              alt="Post"
              className="img-fluid rounded w-100"
              style={{ maxHeight: "500px", objectFit: "cover" }}
            />
          </div>
        )}

        <PostActions 
          post={post}
          currentUserId={currentUserId}
          token={token}
          onToggleComments={handleToggleComments}
          onReactionUpdate={onReactionUpdate}
        />

        {openCommentPostId === post.postId && (
          <CommentSection post={post} token={token} />
        )}
      </div>
    </div>
  );
}
