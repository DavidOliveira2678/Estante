package br.com.estante.dto;

import br.com.estante.entity.BookRatingStats;
import java.math.BigDecimal;

public record BookRatingStatsResponseDto(Long bookId, Long ratingCount, BigDecimal averageRating) {
    public static BookRatingStatsResponseDto from(BookRatingStats s) {
        return new BookRatingStatsResponseDto(s.getBookId(), s.getRatingCount(), s.getAverageRating());
    }
}