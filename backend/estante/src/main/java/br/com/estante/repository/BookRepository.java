package br.com.estante.repository;

import br.com.estante.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE b.isbn10 = :input OR b.isbn13 = :input")
    List<Book> findByIsbn10OrIsbn13(@Param("input") String input);

    Optional<Book> findByExternalIdAndExternalSource(String externalId, String externalSource);

    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    List<Book> findByPublisher(String publisher);

    List<Book> findByTitle(String title);

    List<Book> findByPublishedYear(Integer publishedYear);
}
