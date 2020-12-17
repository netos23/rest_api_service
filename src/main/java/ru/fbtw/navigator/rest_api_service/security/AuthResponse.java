package ru.fbtw.navigator.rest_api_service.security;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.fbtw.navigator.rest_api_service.response.BaseResponse;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResponse implements BaseResponse {
    String token;

    public AuthResponse(String token) {
        this.token = token;
    }
}
