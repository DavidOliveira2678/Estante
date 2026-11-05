package br.com.estante.dto;

import br.com.estante.entity.Author;

public record AuthorResponseDto(Long id, String name, String bio, String externalId) {
    public static AuthorResponseDto from(Author a) {
        return new AuthorResponseDto(a.getId(), a.getName(), a.getBio(), a.getExternalId());
    }
}
