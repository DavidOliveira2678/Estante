package br.com.estante.dto;

import br.com.estante.entity.UserBook;
import br.com.estante.entity.enums.ReadingStatus;
import java.time.Instant;
import java.time.LocalDate;

public record UserBookResponseDto(Long id, Long userId, Long bookId, ReadingStatus status, Integer currentPage,
                                  LocalDate startedAt, LocalDate finishedAt, Instant updatedAt) {

    public static UserBookResponseDto from(UserBook ub) {
        return new UserBookResponseDto(ub.getId(), ub.getUserId(), ub.getBookId(), ub.getStatus(),
                ub.getCurrentPage(), ub.getStartedAt(), ub.getFinishedAt(), ub.getUpdatedAt());
    }
}