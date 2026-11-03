package br.com.estante.service;

import br.com.estante.dto.AuthorRequestDto;
import br.com.estante.dto.AuthorResponseDto;
import br.com.estante.entity.Author;
import br.com.estante.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Transactional
    public AuthorResponseDto create(AuthorRequestDto request) {
        if (request.externalId() != null && authorRepository.findByExternalId(request.externalId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Autor com esse externalId já existe");
        }
        Author author = Author.builder()
                .name(request.name().trim())
                .bio(request.bio())
                .externalId(request.externalId())
                .build();
        return AuthorResponseDto.from(authorRepository.save(author));
    }

    @Transactional(readOnly = true)
    public List<AuthorResponseDto> search(String name) {
        List<Author> authors = (name == null || name.isBlank())
                ? authorRepository.findAll()
                : authorRepository.findByNameContainingIgnoreCase(name.trim());
        return authors.stream().map(AuthorResponseDto::from).toList();
    }

    @Transactional(readOnly = true)
    public AuthorResponseDto findById(Long id) {
        return AuthorResponseDto.from(findOrThrow(id));
    }

    @Transactional
    public AuthorResponseDto update(Long id, AuthorRequestDto request) {
        Author author = findOrThrow(id);
        if (request.externalId() != null) {
            authorRepository.findByExternalId(request.externalId()).ifPresent(other -> {
                if (!other.getId().equals(id)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Autor com esse externalId já existe");
                }
            });
        }
        author.setName(request.name().trim());
        author.setBio(request.bio());
        author.setExternalId(request.externalId());
        return AuthorResponseDto.from(authorRepository.save(author));
    }

    @Transactional
    public void delete(Long id) {
        Author author = findOrThrow(id);
        try {
            authorRepository.delete(author);
            authorRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Autor está associado a livros");
        }
    }

    private Author findOrThrow(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor não encontrado"));
    }
}
