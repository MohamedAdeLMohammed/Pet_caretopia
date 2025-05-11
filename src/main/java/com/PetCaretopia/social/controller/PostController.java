package com.PetCaretopia.social.controller;

import com.PetCaretopia.social.DTO.PostDTO;
import com.PetCaretopia.social.Service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/social/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    public ResponseEntity<List<PostDTO>> getPostsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getPostsByUser(userId));
    }

    // ✅ الميثود المعتمدة لإنشاء Post مع صور باستخدام Multipart
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    public ResponseEntity<PostDTO> createPostWithImages(
            @RequestPart("post") @Valid PostDTO dto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        return ResponseEntity.ok(postService.createPostWithMultipart(dto, images));
    }
    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'SERVICE_PROVIDER')")
    public ResponseEntity<PostDTO> updatePost(
            @PathVariable Long postId,
            @RequestPart("post") @Valid PostDTO dto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        return ResponseEntity.ok(postService.updatePost(postId, dto, images));
    }

}
