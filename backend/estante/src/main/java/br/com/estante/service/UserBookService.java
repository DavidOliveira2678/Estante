package br.com.estante.service;

import br.com.estante.dto.UserBookRequestDto;
import br.com.estante.dto.UserBookResponseDto;
import br.com.estante.dto.UserBookUpdateRequestDto;
import br.com.estante.entity.Book;
import br.com.estante.entity.UserBook;
import br.com.estante.entity.enums.ActivityType;
import br.com.estante.entity.enums.ReadingStatus;
import br.com.estante.event.AtividadeEvent;
import br.com.estante.repository.BookRepository;
import br.com.estante.repository.UserBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBookService {

    private final UserBookRepository userBookRepository;
    private final BookRepository bookRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public UserBookResponseDto add(Long userId, UserBookRequestDto request) {
        Book book = findBook(request.bookId());
        if (userBookRepository.findByUserIdAndBookId(userId, request.bookId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Livro já está na sua estante");
        }
        UserBook userBook = UserBook.builder()
                .userId(userId)
                .bookId(request.bookId())
                .build();
        apply(userBook, book, request.status(), request.currentPage(), request.startedAt(), request.finishedAt());
        UserBook salvo = userBookRepository.save(userBook);
        if (request.status() == ReadingStatus.READ) {
            eventPublisher.publishEvent(new AtividadeEvent(userId, ActivityType.BOOK_FINISHED, request.bookId()));
        }
        return UserBookResponseDto.from(salvo);
    }

    @Transactional
    public UserBookResponseDto update(Long userId, Long bookId, UserBookUpdateRequestDto request) {
        UserBook userBook = findOrThrow(userId, bookId);
        ReadingStatus anterior = userBook.getStatus();
        Book book = findBook(bookId);
        apply(userBook, book, request.status(), request.currentPage(), request.startedAt(), request.finishedAt());
        UserBook salvo = userBookRepository.save(userBook);
        if (request.status() == ReadingStatus.READ && anterior != ReadingStatus.READ) {
            eventPublisher.publishEvent(new AtividadeEvent(userId, ActivityType.BOOK_FINISHED, bookId));
        }
        return UserBookResponseDto.from(salvo);
    }

    @Transactional(readOnly = true)
    public List<UserBookResponseDto> list(Long userId, ReadingStatus status) {
        List<UserBook> items = (status == null)
                ? userBookRepository.findByUserId(userId)
                : userBookRepository.findByUserIdAndStatus(userId, status);
        return items.stream().map(UserBookResponseDto::from).toList();
    }

    @Transactional(readOnly = true)
    public UserBookResponseDto findOne(Long userId, Long bookId) {
        return UserBookResponseDto.from(findOrThrow(userId, bookId));
    }

    @Transactional
    public void remove(Long userId, Long bookId) {
        userBookRepository.delete(findOrThrow(userId, bookId));
    }

    private void apply(UserBook ub, Book book, ReadingStatus status, Integer currentPage,
                       LocalDate startedAt, LocalDate finishedAt) {
        LocalDate today = LocalDate.now();

        if (status == ReadingStatus.READING && startedAt == null) startedAt = today;
        if (status == ReadingStatus.READ) {
            if (finishedAt == null) finishedAt = today;
            if (currentPage == null && book.getPageCount() != null) currentPage = book.getPageCount();
        }

        if (currentPage != null && book.getPageCount() != null && currentPage > book.getPageCount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Página atual maior que o total de páginas");
        }
        if (startedAt != null && finishedAt != null && finishedAt.isBefore(startedAt)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de término anterior à de início");
        }

        ub.setStatus(status);
        ub.setCurrentPage(currentPage);
        ub.setStartedAt(startedAt);
        ub.setFinishedAt(finishedAt);
    }

    private UserBook findOrThrow(Long userId, Long bookId) {
        return userBookRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não está na estante"));
    }

    private Book findBook(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado"));
    }
}
