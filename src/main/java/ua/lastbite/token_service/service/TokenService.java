package ua.lastbite.token_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lastbite.token_service.dto.TokenRequest;
import ua.lastbite.token_service.model.Token;
import ua.lastbite.token_service.repository.TokenRepository;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
public class TokenService {

    private static final long TOKEN_EXPIRATION_TIME = 24 * 60 * 60;

    @Autowired
    private TokenRepository tokenRepository;

    public String generateToken(TokenRequest request) {
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
}