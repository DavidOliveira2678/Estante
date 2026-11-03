package br.com.estante.service;

import br.com.estante.dto.UpdateProfileRequestDto;
import br.com.estante.dto.UserResponseDto;
import br.com.estante.entity.User;
import br.com.estante.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponseDto getById(Long id) {
        return UserResponseDto.from(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public UserResponseDto getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        return UserResponseDto.from(user);
    }

    @Transactional
    public UserResponseDto updateProfile(Long userId, UpdateProfileRequestDto request) {
        User user = findOrThrow(userId);
        if (request.displayName() != null) user.setDisplayName(request.displayName());
        if (request.bio() != null) user.setBio(request.bio());
        if (request.avatarUrl() != null) user.setAvatarUrl(request.avatarUrl());
        return UserResponseDto.from(userRepository.save(user));
    }

    private User findOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> search(String q, Pageable pageable) {
        String termo = q.trim();
        return userRepository
                .findByUsernameContainingIgnoreCaseOrDisplayNameContainingIgnoreCase(termo, termo, pageable)
                .map(UserResponseDto::from);
    }
}
