package br.com.estante.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "book_rating_stats")
public class BookRatingStats {

    @Id
    @Column(name = "book_id")
    private Long bookId; // sem @GeneratedValue: é o id do livro

    @Column(name = "rating_count", nullable = false)
    @Builder.Default
    private Long ratingCount = 0L;

    @Column(name = "rating_sum", nullable = false)
    @Builder.Default
    private Long ratingSum = 0L;

    @Column(name = "average_rating", nullable = false, precision = 3, scale = 2)
    @Builder.Default
    private BigDecimal averageRating = BigDecimal.ZERO;
}
