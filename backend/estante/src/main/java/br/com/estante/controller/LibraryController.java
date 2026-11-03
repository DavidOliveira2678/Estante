package br.com.estante.controller;

import br.com.estante.dto.UserBookRequestDto;
import br.com.estante.dto.UserBookResponseDto;
import br.com.estante.dto.UserBookUpdateRequestDto;
import br.com.estante.entity.enums.ReadingStatus;
import br.com.estante.security.UserDetailsImplementation;
import br.com.estante.service.UserBookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
public class LibraryController {

    private final UserBookService userBookService;

    @GetMapping
    public List<UserBookResponseDto> list(@AuthenticationPrincipal UserDetailsImplementation principal,
                                       @RequestParam(required = false) ReadingStatus status) {
        return userBookService.list(principal.getUser().getId(), status);
    }

    @GetMapping("/{bookId}")
    public UserBookResponseDto findOne(@AuthenticationPrincipal UserDetailsImplementation principal,
                                    @PathVariable Long bookId) {
        return userBookService.findOne(principal.getUser().getId(), bookId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserBookResponseDto add(@AuthenticationPrincipal UserDetailsImplementation principal,
                                @Valid @RequestBody UserBookRequestDto request) {
        return userBookService.add(principal.getUser().getId(), request);
    }

    @PutMapping("/{bookId}")
    public UserBookResponseDto update(@AuthenticationPrincipal UserDetailsImplementation principal,
                                   @PathVariable Long bookId,
                                   @Valid @RequestBody UserBookUpdateRequestDto request) {
        return userBookService.update(principal.getUser().getId(), bookId, request);
    }

    @DeleteMapping("/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@AuthenticationPrincipal UserDetailsImplementation principal,
                       @PathVariable Long bookId) {
        userBookService.remove(principal.getUser().getId(), bookId);
    }
}
