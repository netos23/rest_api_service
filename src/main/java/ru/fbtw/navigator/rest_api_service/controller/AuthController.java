package ru.fbtw.navigator.rest_api_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.fbtw.navigator.rest_api_service.domain.User;
import ru.fbtw.navigator.rest_api_service.security.*;
import ru.fbtw.navigator.rest_api_service.service.UserService;

@RestController
public class AuthController {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    public AuthController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/auth")
    public BaseResponse auth(@RequestBody AuthRequest request) {
        User user = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        if (user != null) {
            String token = jwtProvider.generateToken(user.getUsername());
            return new AuthResponse(token);
        }
        return new WrongResponse("Permisssion denied", 403);
    }
}
