package com.restaurant.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

public class JwtTokenFilter extends GenericFilterBean {
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
        String headerValue = ((HttpServletRequest) req).getHeader("Authorization");
        try {
            getBearerToken(headerValue).flatMap(userDetailsService::loadUserByJwtToken)
                    .ifPresent(userDetails -> SecurityContextHolder.getContext().setAuthentication(
                            new PreAuthenticatedAuthenticationToken(userDetails, "", userDetails.getAuthorities())));
        } catch (IllegalArgumentException exception) {
            logger.error("User is not authenticated or token is not valid", exception);
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "Unauthorized user");
        }
        filterChain.doFilter(req, res);
    }

    private Optional<String> getBearerToken(String headerVal) {
        if (headerVal != null && headerVal.startsWith(BEARER)) {
            return Optional.of(headerVal.replace(BEARER, "").trim());
        }
        throw new IllegalArgumentException("Bearer token is not valid");
    }
}
