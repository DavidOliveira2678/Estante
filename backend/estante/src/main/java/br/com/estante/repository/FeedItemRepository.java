package br.com.estante.repository;

import br.com.estante.entity.FeedItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedItemRepository extends JpaRepository<FeedItem, Long> {
    Page<FeedItem> findByUserId(Long userId, Pageable pageable);
}