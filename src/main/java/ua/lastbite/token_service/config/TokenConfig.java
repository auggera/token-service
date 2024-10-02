package ua.lastbite.token_service.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class TokenConfig {

    @Value("${token.expiration-time}")
    private long tokenExpirationTime;
}
