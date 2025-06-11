package com.PetCaretopia.social.controller;

import com.PetCaretopia.Security.Service.CustomUserDetails;
import com.PetCaretopia.social.DTO.ReactionDTO;
import com.PetCaretopia.social.Service.ReactionService;
import com.PetCaretopia.social.entity.ReactionType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/social/reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    @PostMapping("/post/{postId}")
    public ResponseEntity<ReactionDTO> reactToPost(
            @PathVariable Long postId,
            @RequestParam String type,
            @AuthenticationPrincipal CustomUserDetails principal) {
        ReactionDTO dto = new ReactionDTO();
        dto.setUserId(principal.getUserId());
        dto.setPostId(postId);
        dto.setType(ReactionType.valueOf(type));
        return ResponseEntity.ok(reactionService.reactToPost(dto));
    }

    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    @PutMapping("/post/{postId}")
    public ResponseEntity<ReactionDTO> updatePostReaction(
            @PathVariable Long postId,
            @RequestParam String type,
            @AuthenticationPrincipal CustomUserDetails principal) {

        ReactionDTO dto = new ReactionDTO();
        dto.setUserId(principal.getUserId());
        dto.setPostId(postId);
        dto.setType(ReactionType.valueOf(type));

        return ResponseEntity.ok(reactionService.updateReactionOnPost(dto));
    }

    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<ReactionDTO> updateCommentReaction(
            @PathVariable Long commentId,
            @RequestParam String type,
            @AuthenticationPrincipal CustomUserDetails principal) {

        ReactionDTO dto = new ReactionDTO();
        dto.setUserId(principal.getUserId());
        dto.setCommentId(commentId);
        dto.setType(ReactionType.valueOf(type));

        return ResponseEntity.ok(reactionService.updateReactionOnComment(dto));
    }


    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    @PostMapping("/comment/{commentId}")
    public ResponseEntity<ReactionDTO> reactToComment(
            @PathVariable Long commentId,
            @RequestParam String type,
            @AuthenticationPrincipal CustomUserDetails principal) {
        ReactionDTO dto = new ReactionDTO();
        dto.setUserId(principal.getUserId());
        dto.setCommentId(commentId);
        dto.setType(ReactionType.valueOf(type));
        return ResponseEntity.ok(reactionService.reactToComment(dto));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<ReactionDTO>> getReactionsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(reactionService.getReactionsByPost(postId));
    }

    @GetMapping("/comment/{commentId}")
    public ResponseEntity<List<ReactionDTO>> getReactionsByComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(reactionService.getReactionsByComment(commentId));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/post/{postId}/user")
    public ResponseEntity<ReactionDTO> getUserReactionOnPost(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(reactionService.getUserReaction(postId, principal.getUserId()));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/comment/{commentId}/user")
    public ResponseEntity<ReactionDTO> getUserReactionOnComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(reactionService.getUserReactionOnComment(commentId, principal.getUserId()));
    }

    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Void> removePostReaction(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails principal) {
        reactionService.removeReactionFromPost(postId, principal.getUserId());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> removeCommentReaction(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails principal) {
        reactionService.removeReactionFromComment(commentId, principal.getUserId());
        return ResponseEntity.noContent().build();
    }
}
