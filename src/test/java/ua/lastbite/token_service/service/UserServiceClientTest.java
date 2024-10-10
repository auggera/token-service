package ua.lastbite.token_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ua.lastbite.token_service.dto.user.UserDto;
import ua.lastbite.token_service.dto.user.UserRole;
import ua.lastbite.token_service.exception.ServiceUnavailableException;
import ua.lastbite.token_service.exception.UserNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
public class UserServiceClientTest {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private UserServiceClient userServiceClient;

    @Value("${user-service.url}")
    private String userServiceUrl;

    private static final Integer USER_ID = 1;
    private UserDto mockedUser;
    private String url;

    @BeforeEach
    void setUp() {
        mockedUser = new UserDto();
        mockedUser.setId(USER_ID);
        mockedUser.setFirstName("John");
        mockedUser.setLastName("Doe");
        mockedUser.setEmail("john@doe.com");
        mockedUser.setRole(UserRole.CUSTOMER);

        url = userServiceUrl + "/api/users/" + USER_ID;
    }

    @Test
    void getUserByIdSuccessfully() {
        Mockito.when(restTemplate.getForObject(url, UserDto.class)).thenReturn(mockedUser);

        UserDto userDto = userServiceClient.getUserById(USER_ID);

        assertNotNull(userDto);
        assertEquals(USER_ID, userDto.getId());
        assertEquals(mockedUser.getFirstName(), userDto.getFirstName());
        assertEquals(mockedUser.getLastName(), userDto.getLastName());
        assertEquals(mockedUser.getEmail(), userDto.getEmail());
        assertEquals(mockedUser.getRole(), userDto.getRole());

        Mockito.verify(restTemplate, Mockito.times(1)).getForObject(url, UserDto.class);
    }

    @Test
    void getUserByIdNotFound() {
        Mockito.when(restTemplate.getForObject(url, UserDto.class))
                .thenThrow(HttpClientErrorException.NotFound.create(HttpStatus.NOT_FOUND, "Exception occurred", null, null, null));

        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () -> userServiceClient.getUserById(USER_ID));

        assertEquals("User with ID " + USER_ID + " not found", userNotFoundException.getMessage());

        Mockito.verify(restTemplate, Mockito.times(1)).getForObject(url, UserDto.class);
    }

    @Test
    void getUserByIdServiceUnavailable() {
        Mockito.when(restTemplate.getForObject(url, UserDto.class))
                .thenThrow(RestClientException.class);

        ServiceUnavailableException exception = assertThrows(ServiceUnavailableException.class, () -> userServiceClient.getUserById(USER_ID));

        assertEquals("Failed to communicate with user-service", exception.getMessage());

        Mockito.verify(restTemplate, Mockito.times(1)).getForObject(url, UserDto.class);
    }
}
