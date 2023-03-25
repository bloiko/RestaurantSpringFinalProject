package com.restaurant.web.filters;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "UserFilter", urlPatterns = {"/*"})
public class UserFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        log.debug("Filter creation starts");
        // do nothing
        log.debug("Filter creation finished");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug("Filter starts");

        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpSession session = servletRequest.getSession();
        String username = (String) session.getAttribute("username");
        log.trace("Get attribute from the session: username_admin --> " + username);

        if (username != null && ("loginMain".equals(servletRequest.getParameter("command")) ||
                "registration".equals(servletRequest.getParameter("command")))) {
            log.info("User " + username + " try to get to the command" + servletRequest.getParameter("command"));
            log.debug("Filter finished");
            ((HttpServletResponse) response).sendRedirect("no-access.html");
            /* ((HttpServletResponse) response).sendRedirect("login-admin.html");*/
        } else {
            log.debug("Filter finished");
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        log.debug("Filter destruction starts");
        // do nothing
        log.debug("Filter destruction finished");
    }
}
