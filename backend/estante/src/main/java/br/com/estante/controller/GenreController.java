package br.com.estante.controller;

import br.com.estante.dto.GenreRequestDto;
import br.com.estante.dto.GenreResponseDto;
import br.com.estante.service.GenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public List<GenreResponseDto> findAll() {
        return genreService.findAll();
    }

    @GetMapping("/{id}")
    public GenreResponseDto findById(@PathVariable Long id) {
        return genreService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenreResponseDto create(@Valid @RequestBody GenreRequestDto request) {
        return genreService.create(request);
    }

    @PutMapping("/{id}")
    public GenreResponseDto update(@PathVariable Long id, @Valid @RequestBody GenreRequestDto request) {
        return genreService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        genreService.delete(id);
    }
}
