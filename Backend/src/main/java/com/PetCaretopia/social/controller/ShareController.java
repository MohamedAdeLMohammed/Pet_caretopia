    package com.PetCaretopia.social.controller;

    import com.PetCaretopia.social.DTO.ShareDTO;

    import com.PetCaretopia.social.Service.ShareService;
    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.security.core.annotation.AuthenticationPrincipal;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    @RestController
    @RequestMapping("/social/shares")
    @RequiredArgsConstructor
    public class ShareController {

        private final ShareService shareService;


        @PostMapping
        @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
        public ResponseEntity<ShareDTO> sharePost(
                @Valid @RequestBody ShareDTO dto,
                @AuthenticationPrincipal com.PetCaretopia.Security.Service.CustomUserDetails principal
        ) {
            dto.setUserId(principal.getUserId());
            return ResponseEntity.ok(shareService.sharePost(dto));
        }


        @GetMapping("/user")
        @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
        public ResponseEntity<List<ShareDTO>> getSharesByUser(
                @AuthenticationPrincipal com.PetCaretopia.Security.Service.CustomUserDetails principal
        ) {
            return ResponseEntity.ok(shareService.getSharesByUser(principal.getUserId()));
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


        @GetMapping("/post/{postId}/exists")
        @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
        public ResponseEntity<Boolean> hasUserSharedPost(
                @PathVariable Long postId,
                @AuthenticationPrincipal com.PetCaretopia.Security.Service.CustomUserDetails principal
        ) {
            return ResponseEntity.ok(shareService.hasUserSharedPost(principal.getUserId(), postId));
        }


        @GetMapping("/recent")
        @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
        public ResponseEntity<List<ShareDTO>> getRecentSharesByUser(
                @AuthenticationPrincipal com.PetCaretopia.Security.Service.CustomUserDetails principal,
                @RequestParam(defaultValue = "5") int limit
        ) {
            return ResponseEntity.ok(shareService.getRecentShares(principal.getUserId(), limit));
        }
    }
