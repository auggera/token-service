package ua.lastbite.token_service.dto.user;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserDto {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
}
