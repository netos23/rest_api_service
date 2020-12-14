package ru.fbtw.navigator.rest_api_service.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import ru.fbtw.navigator.rest_api_service.domain.User;
import ru.fbtw.navigator.rest_api_service.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.springframework.util.StringUtils.hasText;

@Component
@Slf4j
public class JwtFilter extends GenericFilterBean {

    public static final String AUTHORIZATION = "Authorization";

    private final JwtProvider jwtProvider;
    private final UserService userService;

    public JwtFilter(JwtProvider jwtProvider, UserService userService) {
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        log.info("Starting filter...");
        String token = getTokenFromRequest((HttpServletRequest) servletRequest);

        if (token != null && jwtProvider.validateToken(token)){
            String login = jwtProvider.getLoginFromToken(token);

            User user = userService.loadUserByUsername(login);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }else{
            log.info("Filter failed: wrong token");
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
