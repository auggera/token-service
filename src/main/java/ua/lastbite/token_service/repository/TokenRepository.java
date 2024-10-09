package ua.lastbite.token_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.lastbite.token_service.model.Token;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByTokenValue(String tokenValue);
}