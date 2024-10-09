package ua.lastbite.token_service.exception;

import lombok.Getter;

@Getter
public class TokenExpiredException extends RuntimeException {

    private final String tokenValue;

    public TokenExpiredException(String tokenValue) {
        super("Token has expired: " + tokenValue);
        this.tokenValue = tokenValue;
    }
}
