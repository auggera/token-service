package ua.lastbite.token_service.exception;

import lombok.Getter;

@Getter
public class TokenAlreadyUsedException extends RuntimeException {

    private final String tokenValue;

    public TokenAlreadyUsedException(String tokenValue) {
        super("Token has already been used: " + tokenValue);
        this.tokenValue = tokenValue;
    }

}
