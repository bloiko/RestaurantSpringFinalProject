package com.restaurant.security.jwt;

import com.restaurant.web.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@WebFilter(filterName = "JwtTokenFilter", urlPatterns = {"/*"})
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
        }
        String headerValue = ((HttpServletRequest) req).getHeader("Authorization");
        getBearerToken(headerValue).flatMap(userDetailsService::loadUserByJwtToken)
                .ifPresent(userDetails -> {
                    SecurityContextHolder.getContext().setAuthentication(
                            new PreAuthenticatedAuthenticationToken(userDetails, "", userDetails.getAuthorities()));
                    LOGGER.info("User is authenticated with userName " + userDetails.getUsername());
                });

        filterChain.doFilter(req, res);
    }

    private Optional<String> getBearerToken(String headerVal) {
        if (headerVal != null && headerVal.startsWith(BEARER)) {
            return Optional.of(headerVal.replace(BEARER, "").trim());
        }
        throw new UnauthorizedException("Unauthorized user");
    }
}
