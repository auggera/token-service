package ua.lastbite.token_service.exception;

import lombok.Getter;

@Getter
public class TokenNotFoundException extends RuntimeException {

    private final String tokenValue;

    public TokenNotFoundException(String tokenValue) {
        super("Token not found: " + tokenValue);
        this.tokenValue = tokenValue;
    }
}
