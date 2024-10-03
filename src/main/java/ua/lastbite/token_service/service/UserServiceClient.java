package ua.lastbite.token_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;
import ua.lastbite.token_service.dto.user.UserDto;

@Service
public class UserServiceClient {

    private final RestTemplate restTemplate;

    @Value("${user-service.url}")
    private String userServiceUrl;

    UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserDto getUserById(Integer id) {
        String url = userServiceUrl + "/users/" + id;
        return restTemplate.getForObject(url, UserDto.class);
    }
}
