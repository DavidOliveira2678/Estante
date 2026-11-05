package br.com.estante.service;

import br.com.estante.dto.BookListItemRequestDto;
import br.com.estante.dto.BookListItemResponseDto;
import br.com.estante.dto.BookListRequestDto;
import br.com.estante.dto.BookListResponseDto;
import br.com.estante.entity.BookList;
import br.com.estante.entity.BookListItem;
import br.com.estante.entity.enums.Visibility;
import br.com.estante.repository.BookListItemRepository;
import br.com.estante.repository.BookListRepository;
import br.com.estante.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookListService {

    private final BookListRepository bookListRepository;
    private final BookListItemRepository itemRepository;
    private final BookRepository bookRepository;

    @Transactional
    public BookListResponseDto create(Long ownerId, BookListRequestDto request) {
        BookList list = BookList.builder()
                .ownerId(ownerId)
                .title(request.title().trim())
                .description(request.description())
                .visibility(request.visibility() != null ? request.visibility() : Visibility.PUBLIC)
                .ranked(Boolean.TRUE.equals(request.ranked()))
                .build();
        return BookListResponseDto.from(bookListRepository.save(list));
    }

    @Transactional(readOnly = true)
    public BookListResponseDto findById(Long listId, Long requesterId) {
        BookList list = bookListRepository.findById(listId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lista não encontrada"));
        boolean isOwner = list.getOwnerId().equals(requesterId);
        if (list.getVisibility() == Visibility.PRIVATE && !isOwner) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lista não encontrada");
        }
        return BookListResponseDto.from(list);
    }

    @Transactional(readOnly = true)
    public List<BookListResponseDto> findByOwner(Long ownerId, Long requesterId) {
        List<BookList> lists = ownerId.equals(requesterId)
                ? bookListRepository.findByOwnerId(ownerId)
                : bookListRepository.findByOwnerIdAndVisibility(ownerId, Visibility.PUBLIC);
        return lists.stream().map(BookListResponseDto::from).toList();
    }

    @Transactional
    public BookListResponseDto update(Long ownerId, Long listId, BookListRequestDto request) {
        BookList list = getOwned(ownerId, listId);
        list.setTitle(request.title().trim());
        list.setDescription(request.description());
        if (request.visibility() != null) list.setVisibility(request.visibility());
        if (request.ranked() != null) list.setRanked(request.ranked());
        return BookListResponseDto.from(bookListRepository.save(list));
    }

    @Transactional
    public void delete(Long ownerId, Long listId) {
        bookListRepository.delete(getOwned(ownerId, listId));
    }

    @Transactional
    public BookListItemResponseDto addItem(Long ownerId, Long listId, BookListItemRequestDto request) {
        BookList list = getOwned(ownerId, listId);
        if (!bookRepository.existsById(request.bookId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado");
        }
        if (itemRepository.existsByListIdAndBookId(listId, request.bookId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Livro já está na lista");
        }
        BookListItem item = BookListItem.builder()
                .list(list)
                .bookId(request.bookId())
                .position(request.position())
                .note(request.note())
                .build();
        item = itemRepository.save(item);
        list.getItems().add(item);
        return BookListItemResponseDto.from(item);
    }

    @Transactional
    public void removeItem(Long ownerId, Long listId, Long itemId) {
        BookList list = getOwned(ownerId, listId);
        boolean removed = list.getItems().removeIf(i -> i.getId().equals(itemId));
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado na lista");
        }
    }

    private BookList getOwned(Long ownerId, Long listId) {
        BookList list = bookListRepository.findById(listId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lista não encontrada"));
        if (!list.getOwnerId().equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Esta lista não é sua");
        }
        return list;
    }
}
