package ua.lastbite.token_service.dto.token;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ua.lastbite.token_service.validation.ValidTokenFormat;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class TokenValidationRequest {

    @NotBlank(message = "Token cannot be empty")
    @ValidTokenFormat
    private String tokenValue;
}
