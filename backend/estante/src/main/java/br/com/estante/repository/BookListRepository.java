package br.com.estante.repository;

import br.com.estante.entity.BookList;
import br.com.estante.entity.enums.Visibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookListRepository extends JpaRepository<BookList, Long> {
    List<BookList> findByOwnerId(Long ownerId);

    List<BookList> findByOwnerIdAndVisibility(Long ownerId, Visibility visibility);
}
