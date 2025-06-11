import React from "react";
import axios from "axios";
import Swal from "sweetalert2";
import "../../CSS/SocialCommunity.css";
function AddPost() {
  const token = sessionStorage.getItem("token");

  const showPostForm = () => {
    Swal.fire({
      title: "Create a Post",
      html: `
        <textarea id="swal-post-content" placeholder="Write something..." rows="4" class="post-textarea"></textarea>
        <input type="file" id="swal-post-images" multiple accept="image/*" class="post-file-input" />
      `,
      showCancelButton: true,
      confirmButtonText: "Post",
      confirmButtonColor: "#21293a",
      preConfirm: async () => {
        const postContent = document.getElementById("swal-post-content").value;
        const imageFiles = document.getElementById("swal-post-images").files;

        if (!postContent.trim()) {
          Swal.showValidationMessage("Post content cannot be empty");
          return false;
        }

        try {
          const formData = new FormData();
          const post = { content: postContent };

          formData.append(
            "post",
            new Blob([JSON.stringify(post)], { type: "application/json" })
          );

          Array.from(imageFiles).forEach((img) => {
            formData.append("images", img);
          });

          await axios.post("https://localhost:8088/social/posts", formData, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });

          return true;
        } catch (error) {
          console.error("Error adding post:", error);
          Swal.showValidationMessage("Failed to add post.");
          return false;
        }
      },
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire("Success", "Post added successfully!", "success");
      }
    });
  };

  return (
    <div className="post-creator">
      <div onClick={showPostForm} className="post-creator-box">
        <div className="post-input-prompt">
          <span className="post-prompt-text">Share something with your community...</span>
        </div>
        <div className="post-creator-footer">
          <button className="post-button">Create Post</button>
        </div>
      </div>
    </div>
  );
}

export default AddPost;