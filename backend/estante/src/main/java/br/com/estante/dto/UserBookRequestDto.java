package br.com.estante.dto;

import br.com.estante.entity.enums.ReadingStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record UserBookRequestDto(
        @NotNull Long bookId,
        @NotNull ReadingStatus status,
        @Min(0) Integer currentPage,
        LocalDate startedAt,
        LocalDate finishedAt
) {

}