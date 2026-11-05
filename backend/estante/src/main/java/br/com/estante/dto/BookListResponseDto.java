package br.com.estante.dto;

import br.com.estante.entity.BookList;
import br.com.estante.entity.enums.Visibility;
import java.time.Instant;
import java.util.List;

public record BookListResponseDto(Long id, Long ownerId, String title, String description, Visibility visibility,
                                  Boolean ranked, Instant createdAt, Instant updatedAt, List<BookListItemResponseDto> items) {

    public static BookListResponseDto from(BookList l) {
        return new BookListResponseDto(l.getId(), l.getOwnerId(), l.getTitle(), l.getDescription(),
                l.getVisibility(), l.getRanked(), l.getCreatedAt(), l.getUpdatedAt(),
                l.getItems().stream().map(BookListItemResponseDto::from).toList());
    }
}