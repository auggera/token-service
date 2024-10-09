package ua.lastbite.token_service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TokenFormatValidator implements ConstraintValidator<ValidTokenFormat, String> {

    @Override
    public boolean isValid(String token, ConstraintValidatorContext context) {
        return token != null && token.matches("[A-Za-z0-9_-]+");
    }
}
