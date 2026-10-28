package br.com.estante.dto;

import br.com.estante.entity.Follow;
import java.time.Instant;

public record FollowResponseDto(Long followerId, Long followingId, Instant createdAt) {
    public static FollowResponseDto from(Follow f) {
        return new FollowResponseDto(f.getFollowerId(), f.getFollowingId(), f.getCreatedAt());
    }
}