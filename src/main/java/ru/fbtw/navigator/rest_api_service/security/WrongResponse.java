package ru.fbtw.navigator.rest_api_service.security;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WrongResponse implements BaseResponse{
    int status;
    String message;

    public WrongResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
