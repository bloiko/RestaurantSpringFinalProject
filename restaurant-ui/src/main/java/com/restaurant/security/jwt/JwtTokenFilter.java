package com.restaurant.security.jwt;

import com.restaurant.web.exception.ForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JwtTokenFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);
    private static final String BEARER = "Bearer";

    private final MyUserDetailsService userDetailsService;

    public JwtTokenFilter(MyUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        LOGGER.info("Process request to check for a JSON Web Token ");
        if (((HttpServletRequest) req).getRequestURI().contains("/security")) {
            filterChain.doFilter(req, res);
            return;
        }
        String headerValue = ((HttpServletRequest) req).getHeader(HttpHeaders.AUTHORIZATION);
        try {
            getBearerToken(headerValue).flatMap(userDetailsService::loadUserByJwtToken)
                    .ifPresent(userDetails -> {
                        SecurityContextHolder.getContext().setAuthentication(
                                new PreAuthenticatedAuthenticationToken(userDetails, "", userDetails.getAuthorities()));
                        LOGGER.info("User is authenticated with userName " + userDetails.getUsername());
                    });
        } catch (ForbiddenException ex) {
            ((HttpServletResponse) res).sendError(HttpStatus.FORBIDDEN.value(), ex.getMessage());
        } catch (IllegalArgumentException ex) {
            ((HttpServletResponse) res).sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        }
        filterChain.doFilter(req, res);
    }

    private Optional<String> getBearerToken(String headerVal) {
        if (headerVal != null && headerVal.startsWith(BEARER)) {
            return Optional.of(headerVal.replace(BEARER, "").trim());
        }
        throw new IllegalArgumentException("Bearer token is not in valid format");
    }
}
