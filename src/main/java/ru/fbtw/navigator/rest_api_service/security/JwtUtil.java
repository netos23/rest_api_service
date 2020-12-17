package ru.fbtw.navigator.rest_api_service.security;

import javax.servlet.http.HttpServletRequest;

import java.util.Objects;

import static org.springframework.util.StringUtils.hasText;

public class JwtUtil {
    public static final String AUTHORIZATION = "Authorization";

    public static String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        return getTokenFromHeader(bearer);
    }


    public static String getTokenFromHeader(String bearer) {
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }


}
