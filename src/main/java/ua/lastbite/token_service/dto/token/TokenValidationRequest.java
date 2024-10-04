package ua.lastbite.token_service.dto.token;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TokenValidationRequest {

    @NotBlank(message = "Token cannot be empty")
    private String token;
}
