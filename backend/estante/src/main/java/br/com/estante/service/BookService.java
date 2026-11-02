package br.com.estante.service;

import br.com.estante.dto.BookRequestDto;
import br.com.estante.dto.BookResponseDto;
import br.com.estante.entity.Author;
import br.com.estante.entity.Book;
import br.com.estante.entity.Genre;
import br.com.estante.repository.AuthorRepository;
import br.com.estante.repository.BookRepository;
import br.com.estante.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @Transactional
    public BookResponseDto create(BookRequestDto request) {
        if (bookRepository.findByExternalIdAndExternalSource(request.externalId(), request.externalSource()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Livro já cadastrado para essa origem");
        }
        Book book = Book.builder()
                .externalId(request.externalId())
                .externalSource(request.externalSource())
                .title(request.title())
                .subtitle(request.subtitle())
                .description(request.description())
                .coverUrl(request.coverUrl())
                .publishedYear(request.publishedYear())
                .isbn10(request.isbn10())
                .isbn13(request.isbn13())
                .pageCount(request.pageCount())
                .language(request.language())
                .publisher(request.publisher())
                .authors(resolveAuthors(request.authorIds()))
                .genres(resolveGenres(request.genreIds()))
                .build();
        return BookResponseDto.from(bookRepository.save(book));
    }

    @Transactional(readOnly = true)
    public Page<BookResponseDto> search(String title, Pageable pageable) {
        Page<Book> page = (title == null || title.isBlank())
                ? bookRepository.findAll(pageable)
                : bookRepository.findByTitleContainingIgnoreCase(title.trim(), pageable);
        return page.map(BookResponseDto::from);
    }

    @Transactional(readOnly = true)
    public BookResponseDto findById(Long id) {
        return BookResponseDto.from(findOrThrow(id));
    }

    @Transactional
    public BookResponseDto update(Long id, BookRequestDto request) {
        Book book = findOrThrow(id);

        boolean pairChanged = !book.getExternalId().equals(request.externalId())
                || !book.getExternalSource().equals(request.externalSource());
        if (pairChanged && bookRepository
                .findByExternalIdAndExternalSource(request.externalId(), request.externalSource()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Livro já cadastrado para essa origem");
        }

        book.setExternalId(request.externalId());
        book.setExternalSource(request.externalSource());
        book.setTitle(request.title());
        book.setSubtitle(request.subtitle());
        book.setDescription(request.description());
        book.setCoverUrl(request.coverUrl());
        book.setPublishedYear(request.publishedYear());
        book.setIsbn10(request.isbn10());
        book.setIsbn13(request.isbn13());
        book.setPageCount(request.pageCount());
        book.setLanguage(request.language());
        book.setPublisher(request.publisher());
        book.setAuthors(resolveAuthors(request.authorIds()));
        book.setGenres(resolveGenres(request.genreIds()));
        return BookResponseDto.from(bookRepository.save(book));
    }

    @Transactional
    public void delete(Long id) {
        bookRepository.delete(findOrThrow(id));
    }

    private Book findOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado"));
    }

    private Set<Author> resolveAuthors(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();
        List<Author> found = authorRepository.findAllById(ids);
        if (found.size() != ids.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Um ou mais autores não existem");
        }
        return new HashSet<>(found);
    }

    private Set<Genre> resolveGenres(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();
        List<Genre> found = genreRepository.findAllById(ids);
        if (found.size() != ids.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Um ou mais gêneros não existem");
        }
        return new HashSet<>(found);
    }
}
