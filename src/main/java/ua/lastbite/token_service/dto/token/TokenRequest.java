package ua.lastbite.token_service.dto.token;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TokenRequest {

    @NotBlank(message = "User ID cannot be empty")
    private Integer userId;
}
