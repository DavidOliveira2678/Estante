package br.com.estante.dto;

import br.com.estante.entity.Review;
import java.time.Instant;

public record ReviewResponseDto(Long id, Long userId, Long bookId, Integer rating, String text,
                                Boolean containsSpoilers, Instant createdAt, Instant updatedAt) {

    public static ReviewResponseDto from(Review r) {
        return new ReviewResponseDto(r.getId(), r.getUserId(), r.getBookId(), r.getRating(),
                r.getText(), r.getContainsSpoilers(), r.getCreatedAt(), r.getUpdatedAt());
    }
}