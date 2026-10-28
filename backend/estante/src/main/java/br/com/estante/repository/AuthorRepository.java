package br.com.estante.repository;


import br.com.estante.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Long, Author> {
}
