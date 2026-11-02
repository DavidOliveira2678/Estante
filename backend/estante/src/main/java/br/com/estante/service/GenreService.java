package br.com.estante.service;

import br.com.estante.dto.GenreRequestDto;
import br.com.estante.dto.GenreResponseDto;
import br.com.estante.entity.Genre;
import br.com.estante.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    @Transactional
    public GenreResponseDto create(GenreRequestDto request) {
        String name = request.name().trim();
        if (genreRepository.existsByNameIgnoreCase(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Gênero já existe");
        }
        Genre genre = genreRepository.save(Genre.builder().name(name).build());
        return GenreResponseDto.from(genre);
    }

    @Transactional(readOnly = true)
    public List<GenreResponseDto> findAll() {
        return genreRepository.findAll().stream().map(GenreResponseDto::from).toList();
    }

    @Transactional(readOnly = true)
    public GenreResponseDto findById(Long id) {
        return GenreResponseDto.from(findOrThrow(id));
    }

    @Transactional
    public GenreResponseDto update(Long id, GenreRequestDto request) {
        Genre genre = findOrThrow(id);
        String name = request.name().trim();
        genreRepository.findByNameIgnoreCase(name).ifPresent(other -> {
            if (!other.getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Gênero já existe");
            }
        });
        genre.setName(name);
        return GenreResponseDto.from(genreRepository.save(genre));
    }

    @Transactional
    public void delete(Long id) {
        Genre genre = findOrThrow(id);
        try {
            genreRepository.delete(genre);
            genreRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Gênero está associado a livros");
        }
    }

    private Genre findOrThrow(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gênero não encontrado"));
    }
}
