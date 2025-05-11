package com.PetCaretopia.social.controller;

import com.PetCaretopia.social.DTO.ShareDTO;

import com.PetCaretopia.social.Service.ShareService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/social/shares")
@RequiredArgsConstructor
public class ShareController {
    private final ShareService shareService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    public ResponseEntity<ShareDTO> sharePost(@Valid @RequestBody ShareDTO dto) {
        return ResponseEntity.ok(shareService.sharePost(dto));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    public ResponseEntity<List<ShareDTO>> getSharesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(shareService.getSharesByUser(userId));
    }

    @GetMapping("/post/{postId}")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    public ResponseEntity<List<ShareDTO>> getSharesByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(shareService.getSharesByPost(postId));
    }

    @DeleteMapping("/{shareId}")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    public ResponseEntity<Void> deleteShare(@PathVariable Long shareId) {
        shareService.deleteShare(shareId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/post/{postId}/exists")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    public ResponseEntity<Boolean> hasUserSharedPost(@PathVariable Long userId, @PathVariable Long postId) {
        return ResponseEntity.ok(shareService.hasUserSharedPost(userId, postId));
    }

    @GetMapping("/user/{userId}/recent")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    public ResponseEntity<List<ShareDTO>> getRecentSharesByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "5") int limit
    ) {
        return ResponseEntity.ok(shareService.getRecentShares(userId, limit));
    }

}
