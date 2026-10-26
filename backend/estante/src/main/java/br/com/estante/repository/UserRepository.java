package br.com.estante.repository;

import br.com.estante.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.email = :input OR u.username = :input")
    Optional<User> findByEmailOrUsername(@Param("input") String input);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
