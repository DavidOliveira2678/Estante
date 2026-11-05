package br.com.estante.controller;

import br.com.estante.dto.UpdateProfileRequestDto;
import br.com.estante.dto.UserResponseDto;
import br.com.estante.security.UserDetailsImplementation;
import br.com.estante.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// Perfil do usuário logado.
@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class MeController {

    private final UserService userService;

    @GetMapping
    public UserResponseDto me(@AuthenticationPrincipal UserDetailsImplementation principal) {
        return userService.getById(principal.getUser().getId());
    }

    @PutMapping
    public UserResponseDto updateProfile(@AuthenticationPrincipal UserDetailsImplementation principal,
                                      @Valid @RequestBody UpdateProfileRequestDto request) {
        return userService.updateProfile(principal.getUser().getId(), request);
    }
}
