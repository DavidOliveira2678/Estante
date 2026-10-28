package br.com.estante.dto;

import br.com.estante.entity.BookListItem;
import java.time.Instant;

public record BookListItemResponseDto(Long id, Long bookId, Integer position, String note, Instant addedAt) {
    public static BookListItemResponseDto from(BookListItem i) {
        return new BookListItemResponseDto(i.getId(), i.getBookId(), i.getPosition(), i.getNote(), i.getAddedAt());
    }
}