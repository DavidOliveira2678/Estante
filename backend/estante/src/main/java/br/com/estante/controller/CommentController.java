package br.com.estante.controller;

import br.com.estante.entity.enums.Role;
import br.com.estante.security.UserDetailsImplementation;
import br.com.estante.service.ReviewCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final ReviewCommentService commentService;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal UserDetailsImplementation principal, @PathVariable Long id) {
        boolean isAdmin = principal.getUser().getRole() == Role.ADMIN;
        commentService.delete(principal.getUser().getId(), isAdmin, id);
    }
}
