package com.restaurant.web.command.login;


import com.restaurant.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Command that process user data from the main login page.
 *
 * @author B.Loiko
 */
@Slf4j
@Controller
@Deprecated
public class LoginMainCommand {
    public static final String COMMAND = "command";
    private final UserService userService;

    public LoginMainCommand(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login-main")
    public String getLoginPage() {
        return "login-main";
    }

    @PostMapping("/login")
    public String execute(@RequestParam String username, @RequestParam String password,
                          HttpServletRequest request) throws ServletException, IOException {
        HttpSession session = request.getSession();
        removePastErrorMessagesIfExist(session);
        if (!userService.isCorrectUser(username, password)) {
            session.setAttribute("message", "Account's Invalid");
            log.trace("Set attribute to the session: message --> " + "Account's Invalid");

            return "login-main";
        }

        userService.login(username, password);
        request.login(username, password); //TODO maybe remove

        log.info("User " + username + " is correct user");
        session.setAttribute("username", username);
        log.trace("Set attribute to the session: username --> " + username);

        if (!"ORDER_IN_CART".equals(session.getAttribute(COMMAND))) {
            return "redirect:/menu";
        }
        session.removeAttribute(COMMAND);
        log.info("Remove attribute from the session: " + COMMAND);

        return "cart";
    }

    private void removePastErrorMessagesIfExist(HttpSession session) {
        String pastMessage = (String) session.getAttribute("message");
        if (pastMessage != null) {
            session.removeAttribute("message");
        }
        log.trace("Remove attribute from the session: message");
    }
}