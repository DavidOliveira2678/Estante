package br.com.estante.service;

import br.com.estante.dto.BookRatingStatsResponseDto;
import br.com.estante.entity.BookRatingStats;
import br.com.estante.repository.BookRatingStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class BookRatingStatsService {

    private final BookRatingStatsRepository statsRepository;

    @Transactional(readOnly = true)
    public BookRatingStatsResponseDto getByBookId(Long bookId) {
        return statsRepository.findById(bookId)
                .map(BookRatingStatsResponseDto::from)
                .orElseGet(() -> new BookRatingStatsResponseDto(bookId, 0L, BigDecimal.ZERO));
    }

    @Transactional
    public void addRating(Long bookId, int rating) {
        BookRatingStats stats = loadOrCreate(bookId);
        stats.setRatingCount(stats.getRatingCount() + 1);
        stats.setRatingSum(stats.getRatingSum() + rating);
        recalculate(stats);
        statsRepository.save(stats);
    }

    @Transactional
    public void changeRating(Long bookId, int oldRating, int newRating) {
        BookRatingStats stats = loadOrCreate(bookId);
        stats.setRatingSum(stats.getRatingSum() - oldRating + newRating);
        recalculate(stats);
        statsRepository.save(stats);
    }

    @Transactional
    public void removeRating(Long bookId, int rating) {
        BookRatingStats stats = loadOrCreate(bookId);
        stats.setRatingCount(Math.max(0, stats.getRatingCount() - 1));
        stats.setRatingSum(Math.max(0, stats.getRatingSum() - rating));
        recalculate(stats);
        statsRepository.save(stats);
    }

    private BookRatingStats loadOrCreate(Long bookId) {
        return statsRepository.findById(bookId)
                .orElseGet(() -> BookRatingStats.builder().bookId(bookId).build());
    }

    private void recalculate(BookRatingStats stats) {
        if (stats.getRatingCount() == 0) {
            stats.setAverageRating(BigDecimal.ZERO);
            return;
        }
        stats.setAverageRating(BigDecimal.valueOf(stats.getRatingSum())
                .divide(BigDecimal.valueOf(stats.getRatingCount() * 2), 2, RoundingMode.HALF_UP));
    }
}
