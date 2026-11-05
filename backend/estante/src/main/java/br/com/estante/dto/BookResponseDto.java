package br.com.estante.dto;

import br.com.estante.entity.Book;
import java.util.Set;
import java.util.stream.Collectors;

public record BookResponseDto(
        Long id, String externalId, String externalSource,
        String title, String subtitle, String description, String coverUrl,
        Integer publishedYear, String isbn10, String isbn13, Integer pageCount,
        String language, String publisher,
        Set<AuthorResponseDto> authors, Set<GenreResponseDto> genres
) {
    public static BookResponseDto from(Book b) {
        return new BookResponseDto(
                b.getId(), b.getExternalId(), b.getExternalSource(),
                b.getTitle(), b.getSubtitle(), b.getDescription(), b.getCoverUrl(),
                b.getPublishedYear(), b.getIsbn10(), b.getIsbn13(), b.getPageCount(),
                b.getLanguage(), b.getPublisher(),
                b.getAuthors().stream().map(AuthorResponseDto::from).collect(Collectors.toSet()),
                b.getGenres().stream().map(GenreResponseDto::from).collect(Collectors.toSet()));
    }
}