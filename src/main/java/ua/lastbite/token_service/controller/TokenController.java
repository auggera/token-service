package ua.lastbite.token_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lastbite.token_service.dto.TokenRequest;
import ua.lastbite.token_service.dto.TokenValidationRequest;
import ua.lastbite.token_service.dto.TokenValidationResponse;
import ua.lastbite.token_service.service.TokenService;

@RestController
@RequestMapping("/api/tokens")
public class TokenController {

    @Autowired
    TokenService tokenService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateToken(@RequestBody TokenRequest request) {
        String token = tokenService.generateToken(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validateToken(@RequestBody TokenValidationRequest request) {
        boolean isValid = tokenService.validateToken(request);
        TokenValidationResponse response = new TokenValidationResponse(isValid, null);

        if (isValid) {
            Integer userId = tokenService.extractUserIdFromToken(request.getToken());
            response.setUserId(userId);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/extract-user")
    public ResponseEntity<Integer> extractUserId(@RequestParam("token") String token) {
        Integer userId = tokenService.extractUserIdFromToken(token);
        return ResponseEntity.ok(userId);
    }

    @PostMapping("/mark-used")
    public ResponseEntity<String> markUsed(@RequestParam("token") TokenValidationRequest request) {
        tokenService.markTokenAsUsed(request.getToken());
        return ResponseEntity.ok("Token marked as used");
    }
}
