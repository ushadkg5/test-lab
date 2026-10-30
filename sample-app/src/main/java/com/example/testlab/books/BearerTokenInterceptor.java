package com.example.testlab.books;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/** Requires the configured bearer token on write requests; GET stays public. */
@Component
public class BearerTokenInterceptor implements HandlerInterceptor {

    private static final Set<String> PROTECTED_METHODS = Set.of("POST", "PUT", "DELETE");

    private final String expectedHeader;

    public BearerTokenInterceptor(@Value("${books.auth.token}") String token) {
        this.expectedHeader = "Bearer " + token;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!PROTECTED_METHODS.contains(request.getMethod())) {
            return true;
        }
        if (expectedHeader.equals(request.getHeader(HttpHeaders.AUTHORIZATION))) {
            return true;
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer");
        return false;
    }
}
