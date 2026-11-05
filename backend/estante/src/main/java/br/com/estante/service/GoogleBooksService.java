package br.com.estante.service;

import br.com.estante.dto.google.GoogleBooksResponseDto;
import br.com.estante.entity.Author;
import br.com.estante.entity.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GoogleBooksService {

    private final RestClient restClient;
private final String apiKey;

public GoogleBooksService(RestClient.Builder restClientBuilder,
                          @Value("${google.books.api.url:https://www.googleapis.com/books/v1/volumes}") String apiUrl,
                          @Value("${google.books.api.key:}") String apiKey) {
    this.restClient = restClientBuilder.baseUrl(apiUrl).build();
    this.apiKey = apiKey;
}

public List<Book> searchBooks(String query) {
    GoogleBooksResponseDto response;
    try {
        response = restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.queryParam("q", query)
                            .queryParam("maxResults", 20)
                            .queryParam("printType", "books");
                    if (apiKey != null && !apiKey.isBlank()) {
                        uriBuilder.queryParam("key", apiKey);
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .body(GoogleBooksResponseDto.class);
    } catch (RestClientException e) {
        System.err.println("Falha ao consultar Google Books: " + e.getMessage());
        return Collections.emptyList();
    }

        return response.items().stream().map(item -> {
            var info = item.volumeInfo();
            Book book = new Book();

            book.setExternalId(item.id());
            book.setExternalSource("GOOGLE_BOOKS");
            book.setTitle(info.title());
            book.setSubtitle(info.subtitle());
            book.setDescription(info.description() != null ? info.description() : "Sinopse indisponível.");
            book.setPageCount(info.pageCount());
            book.setLanguage(info.language());
            book.setPublisher(info.publisher());

            if (info.publishedDate() != null && info.publishedDate().length() >= 4) {
                book.setPublishedYear(Integer.parseInt(info.publishedDate().substring(0, 4)));
            }

            if (info.imageLinks() != null && info.imageLinks().thumbnail() != null) {
                book.setCoverUrl(info.imageLinks().thumbnail().replace("http:", "https:"));
            }

            if (info.industryIdentifiers() != null) {
                for (var id : info.industryIdentifiers()) {
                    if ("ISBN_10".equals(id.type())) book.setIsbn10(id.identifier());
                    if ("ISBN_13".equals(id.type())) book.setIsbn13(id.identifier());
                }
            }

            if (info.authors() != null) {
                Set<Author> bookAuthors = new HashSet<>();
                for (String authorName : info.authors()) {
                    Author newAuthor = Author.builder()
                            .name(authorName)
                            .externalId("GOOGLE_" + authorName.replaceAll("\\s+", "_").toUpperCase())
                            .build();
                    bookAuthors.add(newAuthor);
                }
                book.setAuthors(bookAuthors);
            }

            return book;
        }).collect(Collectors.toList());
    }
}