package br.com.estante.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewUpdateRequestDto(
        @NotNull @Min(1) @Max(10) Integer rating,
        @Size(max = 10000) String text,
        Boolean containsSpoilers
) {

}