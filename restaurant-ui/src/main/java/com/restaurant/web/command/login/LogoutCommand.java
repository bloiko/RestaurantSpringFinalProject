package com.restaurant.web.command.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Command that log out user.
 *
 * @author B.Loiko
 */
@Slf4j
@Controller
@Deprecated
public class LogoutCommand {
    @GetMapping("/logout")
    public String execute(HttpServletRequest request) throws IOException, ServletException {
        log.debug("Controller starts");
        HttpSession session = request.getSession();
        session.removeAttribute("username");
        log.trace("Session attribute was removed : username");
        session.removeAttribute("cart");
        log.trace("Session attribute was removed : cart");
        request.logout();
        log.debug("Controller finished");
        return "redirect:/menu";
    }
}
