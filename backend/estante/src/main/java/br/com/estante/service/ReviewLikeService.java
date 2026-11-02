package br.com.estante.service;

import br.com.estante.entity.ReviewLike;
import br.com.estante.repository.ReviewLikeRepository;
import br.com.estante.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public void like(Long userId, Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resenha não encontrada");
        }
        if (reviewLikeRepository.existsByReviewIdAndUserId(reviewId, userId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Você já curtiu esta resenha");
        }
        reviewLikeRepository.save(ReviewLike.builder().reviewId(reviewId).userId(userId).build());
    }

    @Transactional
    public void unlike(Long userId, Long reviewId) {
        ReviewLike like = reviewLikeRepository.findByReviewIdAndUserId(reviewId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curtida não encontrada"));
        reviewLikeRepository.delete(like);
    }

    @Transactional(readOnly = true)
    public long count(Long reviewId) {
        return reviewLikeRepository.countByReviewId(reviewId);
    }

    @Transactional(readOnly = true)
    public boolean hasLiked(Long userId, Long reviewId) {
        return reviewLikeRepository.existsByReviewIdAndUserId(reviewId, userId);
    }
}
