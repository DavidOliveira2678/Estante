package br.com.estante.controller;

import br.com.estante.dto.BookListResponseDto;
import br.com.estante.dto.FollowResponseDto;
import br.com.estante.dto.ReviewResponseDto;
import br.com.estante.dto.UserBookResponseDto;
import br.com.estante.dto.UserResponseDto;
import br.com.estante.entity.enums.ReadingStatus;
import br.com.estante.security.UserDetailsImplementation;
import br.com.estante.service.BookListService;
import br.com.estante.service.FollowService;
import br.com.estante.service.ReviewService;
import br.com.estante.service.UserBookService;
import br.com.estante.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final UserBookService userBookService;
    private final BookListService listService;
    private final FollowService followService;

    @GetMapping("/{id}")
    public UserResponseDto findById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping("/username/{username}")
    public UserResponseDto findByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }

    @GetMapping("/{id}/reviews")
    public Page<ReviewResponseDto> reviews(@PathVariable Long id, Pageable pageable) {
        return reviewService.findByUser(id, pageable);
    }

    @GetMapping("/{id}/library")
    public List<UserBookResponseDto> library(@PathVariable Long id,
                                          @RequestParam(required = false) ReadingStatus status) {
        return userBookService.list(id, status);
    }

    @GetMapping("/{id}/lists")
    public List<BookListResponseDto> lists(@AuthenticationPrincipal UserDetailsImplementation principal,
                                        @PathVariable Long id) {
        Long requesterId = principal != null ? principal.getUser().getId() : null;
        return listService.findByOwner(id, requesterId);
    }

    @GetMapping("/{id}/followers")
    public Page<FollowResponseDto> followers(@PathVariable Long id, Pageable pageable) {
        return followService.followers(id, pageable);
    }

    @GetMapping("/{id}/following")
    public Page<FollowResponseDto> following(@PathVariable Long id, Pageable pageable) {
        return followService.following(id, pageable);
    }

    @GetMapping("/{id}/follow")
    public Map<String, Boolean> isFollowing(@AuthenticationPrincipal UserDetailsImplementation principal,
                                            @PathVariable Long id) {
        boolean following = principal != null && followService.isFollowing(principal.getUser().getId(), id);
        return Map.of("following", following);
    }

    @PostMapping("/{id}/follow")
    @ResponseStatus(HttpStatus.CREATED)
    public FollowResponseDto follow(@AuthenticationPrincipal UserDetailsImplementation principal,
                                 @PathVariable Long id) {
        return followService.follow(principal.getUser().getId(), id);
    }

    @DeleteMapping("/{id}/follow")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unfollow(@AuthenticationPrincipal UserDetailsImplementation principal,
                         @PathVariable Long id) {
        followService.unfollow(principal.getUser().getId(), id);
    }
}
