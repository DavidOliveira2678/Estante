package br.com.estante.dto;

import br.com.estante.entity.enums.Visibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookListRequestDto(
        @NotBlank @Size(max = 255) String title,
        String description,
        Visibility visibility,
        Boolean ranked
) {

}