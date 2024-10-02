package ua.lastbite.token_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lastbite.token_service.dto.TokenRequest;
import ua.lastbite.token_service.model.Token;
import ua.lastbite.token_service.repository.TokenRepository;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {

    private static final long TOKEN_EXPIRATION_TIME = 24 * 60 * 60;
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);


    @Autowired
    private TokenRepository tokenRepository;

    public String generateToken(TokenRequest request) {
        LOGGER.info("Generating token for user ID: {}", request.getUserId());

        String tokenValue = Base64.getEncoder()
                .encodeToString((request.getUserId() + ":" + UUID.randomUUID()).getBytes());

        Token token = new Token();
        token.setTokenValue(tokenValue);
        token.setUserId(request.getUserId());
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusSeconds(TOKEN_EXPIRATION_TIME));
        token.setUsed(false);

        tokenRepository.save(token);
        return tokenValue;
    }

    public boolean validateToken(String tokenValue) {
        LOGGER.info("Validating token: {}", tokenValue);

        Optional<Token> tokenOpt = tokenRepository.findByTokenValue(tokenValue);

        if (tokenOpt.isPresent()) {
            Token token = tokenOpt.get();

            return token.getExpiresAt().isAfter(LocalDateTime.now()) && !token.isUsed();
        }
        return false;
    }

    public Integer extractUserIdFromToken(String tokenValue) {
        LOGGER.info("Extracting user ID from token: {}", tokenValue);
        Optional<Token> tokenOpt = tokenRepository.findByTokenValue(tokenValue);

        if (tokenOpt.isPresent()) {
            return tokenOpt.get().getUserId();
        }

        throw new IllegalArgumentException("Invalid token");
    }

    public void markTokenAsUsed(String tokenValue) {
        LOGGER.info("Marking token as used: {}", tokenValue);
        Optional<Token> tokenOpt = tokenRepository.findByTokenValue(tokenValue);

        if (tokenOpt.isPresent()) {
            Token token = tokenOpt.get();
            token.setUsed(true);
            tokenRepository.save(token);
        }

        throw new IllegalArgumentException("Invalid token");
    }
}