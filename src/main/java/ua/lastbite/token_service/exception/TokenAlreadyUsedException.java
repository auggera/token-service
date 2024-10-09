package ua.lastbite.token_service.exception;

public class TokenAlreadyUsedException extends RuntimeException {
    public TokenAlreadyUsedException(String tokenValue) {
        super("Token has already been used: " + tokenValue);
    }
}
