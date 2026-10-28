package br.com.estante.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GenreRequestDto(
        @NotBlank @Size(max = 100)
        String name
) {

}
