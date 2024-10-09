package ua.lastbite.token_service.dto.token;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import ua.lastbite.token_service.validation.ValidTokenFormat;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class TokenValidationRequest {

    @NotBlank(message = "Token cannot be empty")
    @Size(min = 10, max = 100, message = "Token length must be between {min} and {max} characters")
    @ValidTokenFormat
    private String tokenValue;
}
