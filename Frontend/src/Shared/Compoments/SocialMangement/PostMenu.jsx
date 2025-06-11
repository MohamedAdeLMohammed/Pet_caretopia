import { useEffect, useRef } from "react";
import axios from "axios";
import Swal from "sweetalert2";

export default function PostMenu({ posts, post, currentUserId, token, onClose }) {
  const menuRef = useRef(null);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (menuRef.current && !menuRef.current.contains(event.target)) {
        onClose();
      }
    };
    document.addEventListener("click", handleClickOutside);
    return () => document.removeEventListener("click", handleClickOutside);
  }, [onClose]);

  const handleDelete = async (postId) => {
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
            headers: { 
              Authorization: `Bearer ${token}`,
              'Content-Type': 'application/json'
            },
          });
          onClose();
          Swal.fire("Deleted!", "Your post has been deleted.", "success");
          // The parent component should handle the posts state update
        } catch (error) {
          console.error("Delete failed:", error);
          Swal.fire("Error", error.response?.data?.message || "Failed to delete post", "error");
        }
      }
    });
  };

  const handleReport = async (postId) => {
    try {
      await axios.post(
        `https://localhost:8088/social/reports`,
        {
          reportedPostId: postId,
          reportReason: "Inappropriate content in post",
        },
        {
          headers: { 
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        }
      );
      onClose();
      Swal.fire("Reported!", "Your post has been reported.", "success");
    } catch (error) {
      console.error("Report failed:", error);
      Swal.fire("Error", error.response?.data?.message || "Failed to report post", "error");
    }
  };

  const handleEdit = async (postId) => {
    const targetPost = posts.find((p) => p.postId === postId);
    if (!targetPost) {
      Swal.fire("Error", "Post not found", "error");
      return;
    }

    await Swal.fire({
      title: "Edit Post",
      html: `
        <textarea id="swal-post-content" rows="4" class="swal2-textarea" placeholder="Edit your post...">${targetPost.content}</textarea>
        <input type="file" id="swal-post-image" class="swal2-file" multiple accept="image/*" />
        <div id="swal-image-preview" style="margin-top: 10px;"></div>
      `,
      showCancelButton: true,
      confirmButtonText: "Save",
      focusConfirm: false,
      didOpen: () => {
        const previewDiv = document.getElementById("swal-image-preview");

        // Show existing images
        if (targetPost.postImages?.length) {
          targetPost.postImages.forEach((img) => {
            const image = document.createElement("img");
            image.src = img.url;
            image.style.maxWidth = "100%";
            image.style.marginTop = "10px";
            previewDiv.appendChild(image);
          });
        }

        // Handle preview of newly uploaded images
        const fileInput = document.getElementById("swal-post-image");
        fileInput.addEventListener("change", (e) => {
          const files = e.target.files;
          previewDiv.innerHTML = ""; // Clear previews
          Array.from(files).forEach((file) => {
            const reader = new FileReader();
            reader.onload = (event) => {
              const img = document.createElement("img");
              img.src = event.target.result;
              img.style.maxWidth = "100%";
              img.style.marginTop = "10px";
              previewDiv.appendChild(img);
            };
            reader.readAsDataURL(file);
          });
        });
      },
      preConfirm: () => {
        const content = document.getElementById("swal-post-content").value;
        const images = document.getElementById("swal-post-image").files;
        return { content, images };
      },
    }).then(async (result) => {
      if (!result.isConfirmed) return;

      try {
        const formData = new FormData();
        const editedPost = { postId, content: result.value.content };

        formData.append(
          "post",
          new Blob([JSON.stringify(editedPost)], { type: "application/json" })
        );

        for (let img of result.value.images) {
          formData.append("images", img);
        }

        await axios.put(`https://localhost:8088/social/posts/${postId}`, formData, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'multipart/form-data'
          },
        });

        Swal.fire("Updated!", "Post updated successfully.", "success");
        onClose();
        // The parent component should handle the posts state update
      } catch (error) {
        console.error("Failed to update post:", error);
        Swal.fire("Error", error.response?.data?.message || "Failed to update post", "error");
      }
    });
  };

  return (
    <div
      ref={menuRef}
      className="menu position-absolute bg-white border rounded shadow p-2"
      style={{ right: 0, zIndex: 1000 }}
    >
      {post.user.userID === currentUserId ? (
        <>
          <button className="dropdown-item" onClick={() => handleEdit(post.postId)}>
            Edit
          </button>
          <button className="dropdown-item" onClick={() => handleDelete(post.postId)}>
            Delete
          </button>
        </>
      ) : (
        <button className="dropdown-item" onClick={() => handleReport(post.postId)}>
          Report
        </button>
      )}
    </div>
  );
}