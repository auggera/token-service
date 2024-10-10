package ua.lastbite.token_service.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class TokenFormatValidatorTest {

    private TokenFormatValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new TokenFormatValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void testValidTokenFormat() {
        String validToken = "abc123+DEF/456=";
        assertTrue(validator.isValid(validToken, context), "Token format should be valid");
    }

    @Test
    void testTokenNull() {
        String nullToken = null;
        assertTrue(validator.isValid(nullToken, context), "Null token should be considered valid to let @NotBlank handle it");
    }

    @Test
    void testTokenEmpty() {
        String emptyToken = "";
        assertTrue(validator.isValid(emptyToken, context), "Empty token should be considered valid to let @NotBlank handle it");
    }

    @Test
    void testTokenWithInvalidCharacters() {
        String invalidToken = "abc123!@#";
        assertFalse(validator.isValid(invalidToken, context), "Token format with special characters should be invalid");
    }

    @Test
    void testTokenWithSpaces() {
        String tokenWithSpaces = "abc123 DEF";
        assertFalse(validator.isValid(tokenWithSpaces, context), "Token format with spaces should be invalid");
    }

    @Test
    void testTokenWithOnlyValidCharacters() {
        String validToken = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/==";
        assertTrue(validator.isValid(validToken, context), "Token containing only valid characters should be valid");
    }

    @Test
    void testTokenTooShort() {
        String shortToken = "abc";
        assertTrue(validator.isValid(shortToken, context), "Let @Size handle the length validation");
    }

    @Test
    void testTokenTooLong() {
        String longToken = "a".repeat(200);
        assertTrue(validator.isValid(longToken, context), "Let @Size handle the length validation");
    }
}
