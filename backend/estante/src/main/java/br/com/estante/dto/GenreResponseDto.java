package br.com.estante.dto;

import br.com.estante.entity.Genre;

public record GenreResponseDto(Long id, String name) {
    public static GenreResponseDto from(Genre g) {
        return new GenreResponseDto(g.getId(), g.getName());
    }
}
