package ua.lastbite.token_service.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ua.lastbite.token_service.model.Token;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByTokenValue(String tokenValue);

    @Modifying
    @Transactional
    @Query("DELETE FROM Token t WHERE t.expiresAt < CURRENT_TIMESTAMP OR t.used = true")
    int deleteExpiredOrUsedTokens();
}