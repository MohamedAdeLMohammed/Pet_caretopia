import { formatDistanceToNow } from "date-fns";

export default function CommentItem({ comment }) {
  return (
    <div className="comment-box p-2 mb-2 border rounded bg-light">
      <div className="d-flex justify-content-between">
        <span
          style={{ color: "#023C5A", textTransform: "uppercase", fontSize: "15px" }}
          className="fw-bold"
        >
          {comment.user?.name || "User"}
        </span>
        <small className="text-muted">
          {formatDistanceToNow(new Date(comment.createdAt), { addSuffix: true })}
        </small>
      </div>
      <p>{comment.content}</p>
      {comment.commentImages?.length > 0 && (
        <img
          src={comment.commentImages[0].url}
          alt="comment"
          className="img-fluid rounded"
          style={{ maxWidth: "150px" }}
        />
      )}
    </div>
  );
}