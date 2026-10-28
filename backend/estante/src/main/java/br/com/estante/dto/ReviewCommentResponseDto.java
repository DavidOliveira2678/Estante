package br.com.estante.dto;

import br.com.estante.entity.ReviewComment;
import java.time.Instant;

public record ReviewCommentResponseDto(Long id, Long reviewId, Long userId, String text, Instant createdAt) {
    public static ReviewCommentResponseDto from(ReviewComment c) {
        return new ReviewCommentResponseDto(c.getId(), c.getReviewId(), c.getUserId(), c.getText(), c.getCreatedAt());
    }
}