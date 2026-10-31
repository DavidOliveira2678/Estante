package br.com.estante.repository;


import br.com.estante.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByNameContainingIgnoreCase(String name);

    /*
    For authors from the external API and not from the database from Estante.
     */
    Optional<Author> findByExternalId(String externalId);
}
