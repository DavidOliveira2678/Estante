package br.com.estante.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(
        @NotBlank @Size(min = 3, max = 30)
        @Pattern(regexp = "^[^@\\s]+$", message = "O username não pode conter '@' nem espaços")
        String username,

        @NotBlank @Email
        String email,

        @NotBlank @Size(min = 8, max = 72)
        String password,

        String displayName
) {}