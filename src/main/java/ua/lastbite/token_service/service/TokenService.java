package ua.lastbite.token_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lastbite.token_service.config.TokenConfig;
import ua.lastbite.token_service.dto.token.TokenRequest;
import ua.lastbite.token_service.dto.token.TokenValidationRequest;
import ua.lastbite.token_service.dto.token.TokenValidationResponse;
import ua.lastbite.token_service.exception.TokenNotFoundException;
import ua.lastbite.token_service.mapper.TokenMapper;
import ua.lastbite.token_service.model.Token;
import ua.lastbite.token_service.repository.TokenRepository;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
public class TokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private TokenMapper tokenMapper;

    @Autowired
    TokenConfig tokenConfig;

    public String generateToken(TokenRequest request) {
        LOGGER.info("Generating token for user ID: {}", request.getUserId());
        Token token = tokenMapper.toEntity(request, tokenConfig.getTokenExpirationTime());
        String tokenValue = generateTokenValue(request.getUserId());
        token.setTokenValue(tokenValue);

        tokenRepository.save(token);
        return tokenValue;
    }

    private String generateTokenValue(Integer userId) {
        return Base64.getEncoder()
                .encodeToString((userId + ":" + UUID.randomUUID()).getBytes());
    }

    public TokenValidationResponse validateToken(TokenValidationRequest request) {
        LOGGER.info("Validating token: {}", request.getToken());

        Token token = tokenRepository.findByTokenValue(request.getToken())
                .orElseThrow(() -> new TokenNotFoundException(request.getToken()));

        if (!isValidToken(token)) {
            LOGGER.info("Token is expired or used: {}", token.getTokenValue());
            return new TokenValidationResponse(false, null);
        }

        LOGGER.info("Token is valid. User ID: {}", token.getUserId());
        markTokenAsUsed(token);

        return new TokenValidationResponse(true, token.getUserId());
    }

    private boolean isValidToken(Token token) {
            return token.getExpiresAt().isAfter(LocalDateTime.now()) && !token.isUsed();
    }

    private void markTokenAsUsed(Token token) {
        LOGGER.info("Marking token as used: {}", token.getTokenValue());
        token.setUsed(true);
        tokenRepository.save(token);
    }
}
