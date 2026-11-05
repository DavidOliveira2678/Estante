package br.com.estante.dto;

import br.com.estante.entity.User;
import java.time.Instant;

public record UserResponseDto(Long id, String username, String displayName, String bio, String avatarUrl, Instant createdAt) {
    public static UserResponseDto from(User u) {
        return new UserResponseDto(u.getId(), u.getUsername(), u.getDisplayName(),
                u.getBio(), u.getAvatarUrl(), u.getCreatedAt());
    }
}
