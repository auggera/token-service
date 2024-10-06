package ua.lastbite.token_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.context.ActiveProfiles;

import ua.lastbite.token_service.config.TokenConfig;
import ua.lastbite.token_service.dto.token.TokenRequest;
import ua.lastbite.token_service.dto.token.TokenValidationRequest;
import ua.lastbite.token_service.dto.token.TokenValidationResponse;
import ua.lastbite.token_service.dto.user.UserDto;
import ua.lastbite.token_service.exception.TokenNotFoundException;
import ua.lastbite.token_service.exception.UserNotFoundException;
import ua.lastbite.token_service.mapper.TokenMapper;
import ua.lastbite.token_service.model.Token;
import ua.lastbite.token_service.repository.TokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private TokenConfig tokenConfig;

    @Mock
    private TokenMapper tokenMapper;

    @Mock
    private UserServiceClient userServiceClient;

    private Token token;
    private TokenRequest tokenRequest;
    private TokenValidationRequest tokenValidationRequest;

    @BeforeEach
    void setUp() {
        token = new Token();
        token.setTokenValue("testToken");
        token.setUserId(1);
        token.setExpiresAt(LocalDateTime.now().plusSeconds(86_400L));
        token.setUsed(false);

        tokenRequest = new TokenRequest(1);
        tokenValidationRequest = new TokenValidationRequest("testToken");
    }

    @Test
    void testGenerateTokenSuccessfully() {
        Mockito.when(tokenMapper.toEntity(tokenRequest, tokenConfig.getTokenExpirationTime()))
                .thenReturn(token);

        Mockito.when(userServiceClient.getUserById(token.getUserId()))
                        .thenReturn(new UserDto());

        Mockito.when(tokenRepository.save(any(Token.class))).thenReturn(token);

        String tokenValue = tokenService.generateToken(tokenRequest);

        assertNotNull(tokenValue);
        assertTrue(token.getExpiresAt().isAfter(LocalDateTime.now()));

        Mockito.verify(userServiceClient, Mockito.times(1)).getUserById(token.getUserId());
        Mockito.verify(tokenRepository, Mockito.times(1)).save(token);
        Mockito.verify(tokenMapper, Mockito.times(1)).toEntity(tokenRequest, tokenConfig.getTokenExpirationTime());
    }

    @Test
    void testGenerateTokenUserNotFound() {
        Mockito.doThrow(new UserNotFoundException(token.getUserId()))
                .when(userServiceClient).getUserById(tokenRequest.getUserId());

        UserNotFoundException exception  = assertThrows(UserNotFoundException.class, () -> tokenService.generateToken(tokenRequest));

        assertEquals(exception.getMessage(), "User with ID 1 not found");
        Mockito.verify(userServiceClient, Mockito.times(1)).getUserById(tokenRequest.getUserId());
        Mockito.verify(tokenRepository, Mockito.never()).save(token);
    }

    @Test
    void testGenerateUniqueTokenForSameUser() {
        Mockito.when(tokenMapper.toEntity(tokenRequest, tokenConfig.getTokenExpirationTime()))
                .thenReturn(token);

        Mockito.when(userServiceClient.getUserById(token.getUserId()))
                .thenReturn(new UserDto());

        Mockito.when(tokenRepository.save(any(Token.class))).thenReturn(token);

        String tokenValue1 = tokenService.generateToken(tokenRequest);
        String tokenValue2 = tokenService.generateToken(tokenRequest);

        assertNotNull(tokenValue1);
        assertNotNull(tokenValue2);
        assertNotEquals(tokenValue1, tokenValue2);
    }

    @Test
    void testValidateTokenSuccessfully() {
        Mockito.when(tokenRepository.findByTokenValue(tokenValidationRequest.getToken()))
                .thenReturn(Optional.of(token));

        TokenValidationResponse response = tokenService.validateToken(tokenValidationRequest);

        assertTrue(response.isValid());
        assertEquals(1, response.getUserId());
        assertTrue(token.isUsed());

        Mockito.verify(tokenRepository, Mockito.times(1)).save(token);
    }

    @Test
    void testValidateTokenNotFound() {
        Mockito.when(tokenRepository.findByTokenValue(tokenValidationRequest.getToken()))
                .thenReturn(Optional.empty());

        TokenNotFoundException exception = assertThrows(TokenNotFoundException.class, () -> tokenService.validateToken(tokenValidationRequest));

        assertEquals("Token not found: " + tokenValidationRequest.getToken(), exception.getMessage());
        Mockito.verify(tokenRepository, Mockito.never()).save(token);
    }

    @Test
    void testValidateTokenExpired() {
        token.setCreatedAt(LocalDateTime.now().minusSeconds(86_401L));
        token.setExpiresAt(token.getCreatedAt().plusSeconds(86_400L));

        Mockito.when(tokenRepository.findByTokenValue(tokenValidationRequest.getToken()))
                .thenReturn(Optional.of(token));

        TokenValidationResponse response = tokenService.validateToken(tokenValidationRequest);

        assertFalse(response.isValid());
        assertNull(response.getUserId());
        assertFalse(token.isUsed());

        Mockito.verify(tokenRepository, Mockito.never()).save(token);
    }

    @Test
    void testValidateTokenIsAlreadyUsed() {
        token.setUsed(true);

        Mockito.when(tokenRepository.findByTokenValue(tokenValidationRequest.getToken()))
                .thenReturn(Optional.of(token));

        TokenValidationResponse response = tokenService.validateToken(tokenValidationRequest);

        assertFalse(response.isValid());
        assertNull(response.getUserId());
        assertTrue(token.isUsed());

        Mockito.verify(tokenRepository, Mockito.never()).save(token);
    }
}
