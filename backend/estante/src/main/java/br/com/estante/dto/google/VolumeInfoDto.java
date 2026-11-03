package br.com.estante.dto.google;

import java.util.List;

public record VolumeInfoDto(
        String title,
        String subtitle,
        List<String> authors,
        String description,
        Integer pageCount,
        String publishedDate,
        String language,
        String publisher,
        List<IndustryIdentifierDto> industryIdentifiers,
        ImageLinksDto imageLinks
) {}
