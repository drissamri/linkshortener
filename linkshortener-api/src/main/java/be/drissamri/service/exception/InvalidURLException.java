package be.drissamri.service.exception;

public class InvalidURLException extends LinkshortenerException {

  public InvalidURLException(String message) {
    super(message);
  }

  public InvalidURLException(String message, Throwable cause) {
    super(message, cause);
  }
}