import React, { useState } from "react";
import axios from "axios";
import Swal from "sweetalert2";

function AddPost() {
  const token = sessionStorage.getItem("token");

  const showPostForm = () => {
    Swal.fire({
      title: "Create a Post",
      html: `
        <textarea id="swal-post-content" placeholder="Write something..." rows="4" style="width:100%; padding:10px; border-radius:5px; border:1px solid #ccc; margin-bottom:10px;"></textarea>
        <input type="file" id="swal-post-images" multiple accept="image/*" />
      `,
      showCancelButton: true,
      confirmButtonText: "Post",
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
    <div className="add-post-section">
      <div onClick={showPostForm} className="cursor-pointer bg-gray-100 p-3 rounded">
        <h4>Start Post</h4>
      </div>
    </div>
  );
}

export default AddPost;
