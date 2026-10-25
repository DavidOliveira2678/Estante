package br.com.estante.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "book_list_item")
public class BookListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "list_id", nullable = false)
    private BookList list;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private Integer position;

    @Column(length = 500)
    private String note;

    @Column(name = "added_at", nullable = false, updatable = false)
    private Instant addedAt;

    @PrePersist
    void onCreate() {
        if (addedAt == null) addedAt = Instant.now();
    }
}