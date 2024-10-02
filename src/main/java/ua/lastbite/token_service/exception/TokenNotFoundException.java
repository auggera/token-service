package ua.lastbite.token_service.exception;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String token) {
        super("Token not found: " + token);
    }
}
