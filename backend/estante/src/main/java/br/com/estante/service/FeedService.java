package br.com.estante.service;

import br.com.estante.dto.FeedItemResponseDto;
import br.com.estante.dto.FeedItemResponseDto.ActorDto;
import br.com.estante.dto.FeedItemResponseDto.BookDto;
import br.com.estante.entity.Activity;
import br.com.estante.entity.Book;
import br.com.estante.entity.FeedItem;
import br.com.estante.entity.Review;
import br.com.estante.entity.User;
import br.com.estante.entity.enums.ActivityType;
import br.com.estante.repository.ActivityRepository;
import br.com.estante.repository.BookRepository;
import br.com.estante.repository.FeedItemRepository;
import br.com.estante.repository.FollowRepository;
import br.com.estante.repository.ReviewRepository;
import br.com.estante.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final ActivityRepository activityRepository;
    private final FeedItemRepository feedItemRepository;
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    @Transactional
    public void registrarAtividade(Long actorId, ActivityType type, Long targetId) {
        Activity activity = activityRepository.save(Activity.builder()
                .actorId(actorId)
                .type(type)
                .targetId(targetId)
                .build());

        List<FeedItem> itens = followRepository.findAllByFollowingId(actorId).stream()
                .map(f -> FeedItem.builder()
                        .userId(f.getFollowerId())
                        .activityId(activity.getId())
                        .build())
                .toList();
        feedItemRepository.saveAll(itens);
    }

    @Transactional(readOnly = true)
    public Page<FeedItemResponseDto> feedDe(Long userId, Pageable pageable) {
        return feedItemRepository.findByUserId(userId, pageable).map(this::toDto);
    }

    private FeedItemResponseDto toDto(FeedItem item) {
        Activity a = activityRepository.findById(item.getActivityId()).orElseThrow();
        User actor = userRepository.findById(a.getActorId()).orElseThrow();

        Book book = null;
        Integer rating = null;
        String text = null;

        if (a.getType() == ActivityType.REVIEW_PUBLISHED) {
            Review review = reviewRepository.findById(a.getTargetId()).orElse(null);
            if (review != null) {
                rating = review.getRating();
                text = review.getText();
                book = bookRepository.findById(review.getBookId()).orElse(null);
            }
        } else if (a.getType() == ActivityType.BOOK_FINISHED) {
            book = bookRepository.findById(a.getTargetId()).orElse(null);
        }

        return new FeedItemResponseDto(
                item.getId(),
                a.getType(),
                a.getCreatedAt(),
                new ActorDto(actor.getId(), actor.getUsername(), actor.getDisplayName(), actor.getAvatarUrl()),
                book == null ? null : new BookDto(book.getId(), book.getTitle(), book.getCoverUrl()),
                rating,
                text
        );
    }
}