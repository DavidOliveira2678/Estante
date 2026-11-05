package br.com.estante.dto;

import jakarta.validation.constraints.Size;

public record UpdateProfileRequestDto(
        @Size(max = 100) String displayName,
        @Size(max = 500) String bio,
        @Size(max = 255) String avatarUrl
) {

}
