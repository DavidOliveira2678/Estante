package br.com.estante.controller;

import br.com.estante.dto.AuthorRequestDto;
import br.com.estante.dto.AuthorResponseDto;
import br.com.estante.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public List<AuthorResponseDto> search(@RequestParam(required = false) String name) {
        return authorService.search(name);
    }

    @GetMapping("/{id}")
    public AuthorResponseDto findById(@PathVariable Long id) {
        return authorService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorResponseDto create(@Valid @RequestBody AuthorRequestDto request) {
        return authorService.create(request);
    }

    @PutMapping("/{id}")
    public AuthorResponseDto update(@PathVariable Long id, @Valid @RequestBody AuthorRequestDto request) {
        return authorService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        authorService.delete(id);
    }
}
