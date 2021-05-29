package com.restaurant.web.filters;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
@Slf4j
@WebFilter(filterName = "SessionLocaleFilter", urlPatterns = {"/*"})
public class SessionLocaleFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug("Filter starts");

        HttpServletRequest req = (HttpServletRequest) request;
        String sessionLocale = req.getParameter("sessionLocale");
        log.trace("Get from the request parameter: sessionLocale --> " + sessionLocale);

        if (sessionLocale != null) {
            req.getSession().setAttribute("lang", sessionLocale);
            log.trace("Set to the request attribute: lang --> " + sessionLocale);
        }

        log.debug("Filter finished");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        log.debug("Filter destruction starts");
        // do nothing
        log.debug("Filter destruction finished");
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        log.debug("Filter creation starts");
        // do nothing
        log.debug("Filter creation finished");
    }
}