package br.com.estante.exception;

public class JwtValidationException extends RuntimeException {
  public JwtValidationException(String message) {
    super(message);
  }
}
