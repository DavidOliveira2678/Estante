package br.com.estante.dto;

import br.com.estante.entity.enums.ActivityType;
import java.time.Instant;

public record FeedItemResponseDto(
        Long id,
        ActivityType type,
        Instant createdAt,
        ActorDto actor,
        BookDto book,
        Integer rating,
        String text
) {
    public record ActorDto(Long id, String username, String displayName, String avatarUrl) {}

    public record BookDto(Long id, String title, String coverUrl) {}
}