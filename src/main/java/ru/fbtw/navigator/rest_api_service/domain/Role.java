package ru.fbtw.navigator.rest_api_service.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    SERVICE,
    USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
