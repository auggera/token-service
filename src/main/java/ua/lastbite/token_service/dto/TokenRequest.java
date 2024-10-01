package ua.lastbite.token_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TokenRequest {

    @NotBlank
    private Integer userId;
}
