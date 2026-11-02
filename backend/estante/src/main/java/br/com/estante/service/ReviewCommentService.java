package br.com.estante.service;

import br.com.estante.dto.ReviewCommentRequestDto;
import br.com.estante.dto.ReviewCommentResponseDto;
import br.com.estante.entity.ReviewComment;
import br.com.estante.repository.ReviewCommentRepository;
import br.com.estante.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ReviewCommentService {

    private final ReviewCommentRepository commentRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public ReviewCommentResponseDto add(Long userId, Long reviewId, ReviewCommentRequestDto request) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resenha não encontrada");
        }
        ReviewComment comment = ReviewComment.builder()
                .reviewId(reviewId)
                .userId(userId)
                .text(request.text().trim())
                .build();
        return ReviewCommentResponseDto.from(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    public Page<ReviewCommentResponseDto> list(Long reviewId, Pageable pageable) {
        return commentRepository.findByReviewId(reviewId, pageable).map(ReviewCommentResponseDto::from);
    }

    @Transactional
    public void delete(Long userId, boolean isAdmin, Long commentId) {
        ReviewComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comentário não encontrado"));
        if (!isAdmin && !comment.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não pode apagar este comentário");
        }
        commentRepository.delete(comment);
    }
}
