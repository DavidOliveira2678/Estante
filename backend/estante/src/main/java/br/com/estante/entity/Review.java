package br.com.estante.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "review",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "book_id"}))
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    /** 1 a 10 (0,5 a 5 estrelas). Valide no Service ou com @Min/@Max. */
    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(name = "contains_spoilers", nullable = false)
    @Builder.Default
    private Boolean containsSpoilers = false;
}
