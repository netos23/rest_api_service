package ru.fbtw.navigator.rest_api_service.security;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthRequest {
    String login;
    String password;

    public AuthRequest() {
    }
}
