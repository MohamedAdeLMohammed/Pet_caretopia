package com.PetCaretopia.social.controller;

import com.PetCaretopia.social.DTO.ReactionDTO;
import com.PetCaretopia.social.Service.ReactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/social/reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    public ResponseEntity<ReactionDTO> reactToPost(@Valid @RequestBody ReactionDTO dto) {
        return ResponseEntity.ok(reactionService.reactToPost(dto));
    }

    @GetMapping("/post/{postId}")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    public ResponseEntity<List<ReactionDTO>> getReactionsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(reactionService.getReactionsByPost(postId));
    }

    @GetMapping("/post/{postId}/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    public ResponseEntity<ReactionDTO> getUserReaction(@PathVariable Long postId, @PathVariable Long userId) {
        return ResponseEntity.ok(reactionService.getUserReaction(postId, userId));
    }

    @DeleteMapping("/post/{postId}/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    public ResponseEntity<Void> removeReaction(@PathVariable Long postId, @PathVariable Long userId) {
        reactionService.removeCommentReaction(postId, userId);
        return ResponseEntity.noContent().build();
    }
}
