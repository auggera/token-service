package ua.lastbite.token_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import ua.lastbite.token_service.dto.token.TokenRequest;
import ua.lastbite.token_service.dto.user.UserDto;
import ua.lastbite.token_service.exception.UserNotFoundException;
import ua.lastbite.token_service.service.TokenService;
import ua.lastbite.token_service.service.UserServiceClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class TokenControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserServiceClient userServiceClient;

    TokenRequest tokenRequest;

    @BeforeEach
    void setUp() {
        tokenRequest = new TokenRequest(1);
    }

    @Test
    void generateTokenSuccessfully() throws Exception {
        Mockito.when(userServiceClient.getUserById(tokenRequest.getUserId()))
                        .thenReturn(new UserDto());

        Mockito.when(tokenService.generateToken(tokenRequest))
                .thenReturn("TokenSample");

        MvcResult result = mockMvc.perform(post("/api/tokens/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tokenRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.notNullValue()))
                .andExpect(content().string(Matchers.not("")))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertEquals("TokenSample", responseContent);
    }

    @Test
    void generateTokenUserNotFound() throws Exception {
        Mockito.doThrow(new UserNotFoundException(1))
                        .when(tokenService).generateToken(tokenRequest);

        mockMvc.perform(post("/api/tokens/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tokenRequest)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with ID 1 not found"));
    }


}
