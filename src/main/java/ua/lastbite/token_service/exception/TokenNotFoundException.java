package ua.lastbite.token_service.exception;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String tokenValue) {
        super("Token not found: " + tokenValue);
    }
}
