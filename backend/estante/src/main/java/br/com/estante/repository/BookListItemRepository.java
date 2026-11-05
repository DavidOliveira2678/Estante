package br.com.estante.repository;

import br.com.estante.entity.BookListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookListItemRepository extends JpaRepository<BookListItem, Long> {
    List<BookListItem> findByListIdOrderByPositionAsc(Long listId);
    boolean existsByListIdAndBookId(Long listId, Long bookId);
}
