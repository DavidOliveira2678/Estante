package br.com.estante.dto.google;

import java.util.List;

public record GoogleBooksResponseDto(List<GoogleBookItemDto> items) {}
