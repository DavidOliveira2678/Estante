package br.com.estante.controller;

import br.com.estante.dto.ReviewCommentRequestDto;
import br.com.estante.dto.ReviewCommentResponseDto;
import br.com.estante.dto.ReviewRequestDto;
import br.com.estante.dto.ReviewResponseDto;
import br.com.estante.dto.ReviewUpdateRequestDto;
import br.com.estante.entity.enums.Role;
import br.com.estante.security.UserDetailsImplementation;
import br.com.estante.service.ReviewCommentService;
import br.com.estante.service.ReviewLikeService;
import br.com.estante.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewLikeService likeService;
    private final ReviewCommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponseDto create(@AuthenticationPrincipal UserDetailsImplementation principal,
                                 @Valid @RequestBody ReviewRequestDto request) {
        return reviewService.create(principal.getUser().getId(), request);
    }

    @GetMapping("/me")
    public ReviewResponseDto mine(@AuthenticationPrincipal UserDetailsImplementation principal,
                               @RequestParam Long bookId) {
        return reviewService.findMine(principal.getUser().getId(), bookId);
    }

    @GetMapping("/{id}")
    public ReviewResponseDto findById(@PathVariable Long id) {
        return reviewService.findById(id);
    }

    @PutMapping("/{id}")
    public ReviewResponseDto update(@AuthenticationPrincipal UserDetailsImplementation principal,
                                 @PathVariable Long id,
                                 @Valid @RequestBody ReviewUpdateRequestDto request) {
        return reviewService.update(principal.getUser().getId(), id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal UserDetailsImplementation principal, @PathVariable Long id) {
        boolean isAdmin = principal.getUser().getRole() == Role.ADMIN;
        reviewService.delete(principal.getUser().getId(), isAdmin, id);
    }

    @PostMapping("/{id}/likes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void like(@AuthenticationPrincipal UserDetailsImplementation principal, @PathVariable Long id) {
        likeService.like(principal.getUser().getId(), id);
    }

    @DeleteMapping("/{id}/likes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlike(@AuthenticationPrincipal UserDetailsImplementation principal, @PathVariable Long id) {
        likeService.unlike(principal.getUser().getId(), id);
    }

    @GetMapping("/{id}/likes/count")
    public Map<String, Long> likeCount(@PathVariable Long id) {
        return Map.of("count", likeService.count(id));
    }

    @GetMapping("/{id}/likes/me")
    public Map<String, Boolean> liked(@AuthenticationPrincipal UserDetailsImplementation principal,
                                      @PathVariable Long id) {
        boolean liked = principal != null && likeService.hasLiked(principal.getUser().getId(), id);
        return Map.of("liked", liked);
    }

    @GetMapping("/{id}/comments")
    public Page<ReviewCommentResponseDto> comments(@PathVariable Long id, Pageable pageable) {
        return commentService.list(id, pageable);
    }

    @PostMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewCommentResponseDto addComment(@AuthenticationPrincipal UserDetailsImplementation principal,
                                            @PathVariable Long id,
                                            @Valid @RequestBody ReviewCommentRequestDto request) {
        return commentService.add(principal.getUser().getId(), id, request);
    }
}
