package br.com.estante.service;

import br.com.estante.dto.ReviewRequestDto;
import br.com.estante.dto.ReviewResponseDto;
import br.com.estante.dto.ReviewUpdateRequestDto;
import br.com.estante.entity.Review;
import br.com.estante.repository.BookRepository;
import br.com.estante.repository.ReviewCommentRepository;
import br.com.estante.repository.ReviewLikeRepository;
import br.com.estante.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import br.com.estante.entity.enums.ActivityType;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewCommentRepository reviewCommentRepository;
    private final BookRepository bookRepository;
    private final BookRatingStatsService statsService;
    private final FeedService feedService;

    @Transactional
    public ReviewResponseDto create(Long userId, ReviewRequestDto request) {
        if (!bookRepository.existsById(request.bookId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado");
        }
        if (reviewRepository.findByUserIdAndBookId(userId, request.bookId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Você já avaliou este livro");
        }
        Review review = Review.builder()
                .userId(userId)
                .bookId(request.bookId())
                .rating(request.rating())
                .text(request.text())
                .containsSpoilers(Boolean.TRUE.equals(request.containsSpoilers()))
                .build();
        review = reviewRepository.save(review);
        statsService.addRating(review.getBookId(), review.getRating());
        feedService.registrarAtividade(userId, ActivityType.REVIEW_PUBLISHED, review.getId());
        return ReviewResponseDto.from(review);
    }

    @Transactional
    public ReviewResponseDto update(Long userId, Long reviewId, ReviewUpdateRequestDto request) {
        Review review = findOrThrow(reviewId);
        if (!review.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não pode editar esta resenha");
        }
        int oldRating = review.getRating();
        review.setRating(request.rating());
        review.setText(request.text());
        review.setContainsSpoilers(Boolean.TRUE.equals(request.containsSpoilers()));
        review = reviewRepository.save(review);
        if (oldRating != review.getRating()) {
            statsService.changeRating(review.getBookId(), oldRating, review.getRating());
        }
        return ReviewResponseDto.from(review);
    }

    @Transactional
    public void delete(Long userId, boolean isAdmin, Long reviewId) {
        Review review = findOrThrow(reviewId);
        if (!isAdmin && !review.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não pode apagar esta resenha");
        }
        reviewLikeRepository.deleteByReviewId(reviewId);
        reviewCommentRepository.deleteByReviewId(reviewId);
        reviewRepository.delete(review);
        statsService.removeRating(review.getBookId(), review.getRating());
    }

    @Transactional(readOnly = true)
    public ReviewResponseDto findById(Long reviewId) {
        return ReviewResponseDto.from(findOrThrow(reviewId));
    }

    @Transactional(readOnly = true)
    public ReviewResponseDto findMine(Long userId, Long bookId) {
        return reviewRepository.findByUserIdAndBookId(userId, bookId)
                .map(ReviewResponseDto::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Você ainda não avaliou este livro"));
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> findByBook(Long bookId, Pageable pageable) {
        return reviewRepository.findByBookId(bookId, pageable).map(ReviewResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> findByUser(Long userId, Pageable pageable) {
        return reviewRepository.findByUserId(userId, pageable).map(ReviewResponseDto::from);
    }

    private Review findOrThrow(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resenha não encontrada"));
    }
}
