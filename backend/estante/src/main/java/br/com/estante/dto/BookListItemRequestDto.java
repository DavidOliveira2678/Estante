package br.com.estante.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookListItemRequestDto(
        @NotNull Long bookId,
        @NotNull @Min(1) Integer position,
        @Size(max = 500) String note
) {}