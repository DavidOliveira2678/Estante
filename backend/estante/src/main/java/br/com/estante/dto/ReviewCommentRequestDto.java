package br.com.estante.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReviewCommentRequestDto(@NotBlank @Size(max = 1000) String text) {

}