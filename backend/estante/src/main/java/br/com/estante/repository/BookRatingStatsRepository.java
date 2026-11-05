package br.com.estante.repository;

import br.com.estante.entity.BookRatingStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRatingStatsRepository extends JpaRepository<BookRatingStats, Long> {
}
