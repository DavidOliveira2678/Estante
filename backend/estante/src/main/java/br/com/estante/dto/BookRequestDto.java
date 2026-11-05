package br.com.estante.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record BookRequestDto(
        @NotBlank String externalId,
        @NotBlank String externalSource,
        @NotBlank @Size(max = 255) String title,
        @Size(max = 255) String subtitle,
        String description,
        String coverUrl,
        @Min(0) Integer publishedYear,
        @Size(min = 10, max = 10) String isbn10,
        @Size(min = 13, max = 13) String isbn13,
        @Positive Integer pageCount,
        String language,
        String publisher,
        Set<Long> authorIds,
        Set<Long> genreIds
) {

}