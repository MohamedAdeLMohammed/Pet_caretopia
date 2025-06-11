import { useState, useEffect } from "react";
import axios from "axios";
import Swal from "sweetalert2";
import { jwtDecode } from "jwt-decode";
import PostItem from "./PostItem";

function Posts() {
  const [posts, setPosts] = useState([]);
  const [userInfo, setUserInfo] = useState("");
  const token = sessionStorage.getItem("token");
  const decode = jwtDecode(token);

  useEffect(() => {
    const token = sessionStorage.getItem("token");
    if (!token) {
      Swal.fire("Error", "No authentication token found", "error");
      return;
    }

    let decode;
    try {
      decode = jwtDecode(token);
    } catch (err) {
      console.error("Token decoding error:", err);
      Swal.fire("Error", "Invalid authentication token", "error");
      return;
    }

    const getPosts = async () => {
      try {
        const response = await axios.get(`https://localhost:8088/social/posts`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setPosts(response.data);
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to fetch posts", "error");
      }
    };

    const getUserInfo = async () => {
      try {
        const response = await axios.get(
          `https://localhost:8088/users/user/${decode.id}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
          }
        );
        setUserInfo(response.data);
      } catch (error) {
        console.error(error);
      }
    };

    getPosts();
    getUserInfo();
  }, [token]);

  const handleReactionUpdate = (postId, newReaction) => {
    setPosts((prevPosts) =>
      prevPosts.map((post) => {
        if (post.postId !== postId) return post;

        const otherReactions = post.reactions?.filter(
          (r) => r.userId !== decode.id
        ) || [];

        return {
          ...post,
          reactions: newReaction ? [...otherReactions, newReaction] : otherReactions,
        };
      })
    );
  };

  return (
    <div className="posts-section d-flex flex-column gap-3">
      {posts.map((post) => (
        <PostItem
          key={post.postId}
          posts={posts} 
          post={post}
          userInfo={userInfo}
          currentUserId={decode.id}
          token={token}
          onReactionUpdate={handleReactionUpdate} // ✅ passed here
        />
      ))}
    </div>
  );
}

export default Posts;
