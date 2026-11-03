package br.com.estante.controller;

import br.com.estante.dto.BookRatingStatsResponseDto;
import br.com.estante.dto.BookRequestDto;
import br.com.estante.dto.BookResponseDto;
import br.com.estante.dto.ReviewResponseDto;
import br.com.estante.service.BookRatingStatsService;
import br.com.estante.service.BookService;
import br.com.estante.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final ReviewService reviewService;
    private final BookRatingStatsService statsService;

    @GetMapping
    public Page<BookResponseDto> search(@RequestParam(required = false) String title, Pageable pageable) {
        return bookService.search(title, pageable);
    }

    @GetMapping("/{id}")
    public BookResponseDto findById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @GetMapping("/{id}/rating")
    public BookRatingStatsResponseDto rating(@PathVariable Long id) {
        return statsService.getByBookId(id);
    }

    @GetMapping("/{id}/reviews")
    public Page<ReviewResponseDto> reviews(@PathVariable Long id, Pageable pageable) {
        return reviewService.findByBook(id, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponseDto create(@Valid @RequestBody BookRequestDto request) {
        return bookService.create(request);
    }

    @PutMapping("/{id}")
    public BookResponseDto update(@PathVariable Long id, @Valid @RequestBody BookRequestDto request) {
        return bookService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }
}
