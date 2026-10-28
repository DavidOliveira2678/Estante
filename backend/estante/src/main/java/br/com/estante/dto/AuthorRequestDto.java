package br.com.estante.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthorRequestDto(
        @NotBlank @Size(max = 255) String name,
        String bio,
        String externalId
) {

}
