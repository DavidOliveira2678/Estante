package br.com.estante.repository;

import br.com.estante.entity.UserBook;
import br.com.estante.entity.enums.ReadingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserBookRepository extends JpaRepository<UserBook, Long> {
    List<UserBook> findByUserId(Long userId);

    List<UserBook> findByUserIdAndStatus(Long userId, ReadingStatus status);

    Optional<UserBook> findByUserIdAndBookId(Long userId, Long bookId);
}
