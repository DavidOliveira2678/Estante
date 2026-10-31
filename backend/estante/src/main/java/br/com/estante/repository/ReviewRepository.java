package br.com.estante.repository;

import br.com.estante.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByUserId(Long userId, Pageable pageable);

    Page<Review> findByBookId(Long bookId, Pageable pageable);

    Optional<Review> findByUserIdAndBookId(Long userId, Long bookId);
}
