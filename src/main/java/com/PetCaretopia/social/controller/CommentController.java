package com.PetCaretopia.social.controller;

import com.PetCaretopia.social.DTO.CommentDTO;
import com.PetCaretopia.social.DTO.ReactionDTO;
import com.PetCaretopia.social.Service.CommentService;
import com.PetCaretopia.social.entity.ReactionType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/social/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/post/{postId}")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    public ResponseEntity<List<CommentDTO>> getCommentsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsForPost(postId));
    }

    @GetMapping("/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.getCommentById(commentId));
    }

    @PostMapping(value = "/multipart", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    public ResponseEntity<CommentDTO> addCommentWithMultipart(
            @RequestPart("comment") @Valid CommentDTO dto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        return ResponseEntity.ok(commentService.createCommentWithMultipart(dto, images));
    }

    // ✅ تعديل تعليق وصوره بـ Multipart
    @PutMapping(value = "/{commentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Long commentId,
            @RequestPart("comment") @Valid CommentDTO dto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        return ResponseEntity.ok(commentService.updateComment(commentId, dto, images));
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{commentId}/react")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    public ResponseEntity<?> reactToComment(
            @PathVariable Long commentId,
            @RequestParam Long userId,
            @RequestParam String type // مثال: "LIKE", "LOVE"
    ) {
        ReactionDTO dto = new ReactionDTO();
        dto.setUserId(userId);
        dto.setCommentId(commentId);
        dto.setType(ReactionType.valueOf(type));
        return ResponseEntity.ok(commentService.reactToComment(dto));
    }
    @GetMapping("/{commentId}/reactions")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    public ResponseEntity<List<ReactionDTO>> getCommentReactions(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.getReactionsByComment(commentId));
    }
    @GetMapping("/{commentId}/reactions/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    public ResponseEntity<ReactionDTO> getUserReactionOnComment(
            @PathVariable Long commentId,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(commentService.getUserCommentReaction(commentId, userId));
    }

    @DeleteMapping("/{commentId}/react")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    public ResponseEntity<Void> removeReactionFromComment(
            @PathVariable Long commentId,
            @RequestParam Long userId
    ) {
        commentService.removeCommentReaction(commentId, userId);
        return ResponseEntity.noContent().build();
    }


}