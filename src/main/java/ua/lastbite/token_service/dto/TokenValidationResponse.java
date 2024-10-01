package ua.lastbite.token_service.dto;

import lombok.AllArgsConstructor;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TokenValidationResponse {

    private boolean valid;
    private Integer userId;
}
