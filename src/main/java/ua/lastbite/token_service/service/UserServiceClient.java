package ua.lastbite.token_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ua.lastbite.token_service.dto.user.UserDto;
import ua.lastbite.token_service.exception.ServiceUnavailableException;
import ua.lastbite.token_service.exception.UserNotFoundException;

@Service
public class UserServiceClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceClient.class);

    private final RestTemplate restTemplate;

    @Value("${user-service.url}")
    private String userServiceUrl;

    UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserDto getUserById(Integer id) {
        String url = userServiceUrl + "/api/users/" + id;
        try {
            LOGGER.info("Requesting user with ID: {}", id);
            UserDto userDto =  restTemplate.getForObject(url, UserDto.class);
            LOGGER.debug("User data retrieved: {}", userDto);
            return userDto;
        } catch (HttpClientErrorException.NotFound e) {
            LOGGER.error("User not found with ID: {}", id);
            throw new UserNotFoundException(id);
        } catch (RestClientException e) {
            LOGGER.error("Error occurred while calling user-service for ID: {}", id, e);
            throw new ServiceUnavailableException("Failed to communicate with user-service");
        }
    }
}
