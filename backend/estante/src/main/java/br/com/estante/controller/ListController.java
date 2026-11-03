package br.com.estante.controller;

import br.com.estante.dto.BookListItemRequestDto;
import br.com.estante.dto.BookListItemResponseDto;
import br.com.estante.dto.BookListRequestDto;
import br.com.estante.dto.BookListResponseDto;
import br.com.estante.security.UserDetailsImplementation;
import br.com.estante.service.BookListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lists")
@RequiredArgsConstructor
public class ListController {

    private final BookListService listService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookListResponseDto create(@AuthenticationPrincipal UserDetailsImplementation principal,
                                   @Valid @RequestBody BookListRequestDto request) {
        return listService.create(principal.getUser().getId(), request);
    }

    @GetMapping("/{id}")
    public BookListResponseDto findById(@AuthenticationPrincipal UserDetailsImplementation principal,
                                     @PathVariable Long id) {
        Long requesterId = principal != null ? principal.getUser().getId() : null;
        return listService.findById(id, requesterId);
    }

    @PutMapping("/{id}")
    public BookListResponseDto update(@AuthenticationPrincipal UserDetailsImplementation principal,
                                   @PathVariable Long id,
                                   @Valid @RequestBody BookListRequestDto request) {
        return listService.update(principal.getUser().getId(), id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal UserDetailsImplementation principal, @PathVariable Long id) {
        listService.delete(principal.getUser().getId(), id);
    }

    @PostMapping("/{id}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public BookListItemResponseDto addItem(@AuthenticationPrincipal UserDetailsImplementation principal,
                                        @PathVariable Long id,
                                        @Valid @RequestBody BookListItemRequestDto request) {
        return listService.addItem(principal.getUser().getId(), id, request);
    }

    @DeleteMapping("/{id}/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(@AuthenticationPrincipal UserDetailsImplementation principal,
                           @PathVariable Long id, @PathVariable Long itemId) {
        listService.removeItem(principal.getUser().getId(), id, itemId);
    }
}
