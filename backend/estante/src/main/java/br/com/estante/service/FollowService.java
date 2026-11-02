package br.com.estante.service;

import br.com.estante.dto.FollowResponseDto;
import br.com.estante.entity.Follow;
import br.com.estante.repository.FollowRepository;
import br.com.estante.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public FollowResponseDto follow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você não pode seguir a si mesmo");
        }
        if (!userRepository.existsById(followingId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }
        if (followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Você já segue este usuário");
        }
        Follow follow = Follow.builder().followerId(followerId).followingId(followingId).build();
        return FollowResponseDto.from(followRepository.save(follow));
    }

    @Transactional
    public void unfollow(Long followerId, Long followingId) {
        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Você não segue este usuário"));
        followRepository.delete(follow);
    }

    @Transactional(readOnly = true)
    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }

    @Transactional(readOnly = true)
    public Page<FollowResponseDto> followers(Long userId, Pageable pageable) {
        return followRepository.findByFollowingId(userId, pageable).map(FollowResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<FollowResponseDto> following(Long userId, Pageable pageable) {
        return followRepository.findByFollowerId(userId, pageable).map(FollowResponseDto::from);
    }
}
