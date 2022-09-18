package com.restaurant.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
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

    private MyUserDetailsService userDetailsService;

    public JwtTokenFilter(MyUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        LOGGER.info("Process request to check for a JSON Web Token ");
        String headerValue = ((HttpServletRequest) req).getHeader("Authorization");
        getBearerToken(headerValue).ifPresent(token -> {
            userDetailsService.loadUserByJwtToken(token).ifPresent(userDetails -> {
                SecurityContextHolder.getContext().setAuthentication(
                        new PreAuthenticatedAuthenticationToken(userDetails, "", userDetails.getAuthorities()));
            });
        });

        filterChain.doFilter(req, res);
    }

    private Optional<String> getBearerToken(String headerVal) {
        if (headerVal != null && headerVal.startsWith(BEARER)) {
            return Optional.of(headerVal.replace(BEARER, "").trim());
        }
        return Optional.empty();
    }
}
