package com.restaurant.web.command.login;

import com.restaurant.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Command that process data from the admin login.
 *
 * @author B.Loiko
 */
@Slf4j
@Controller
public class LoginAdminCommand {
    @Autowired
    private UserService userService;

    @GetMapping("/login-admin")
    public String execute(HttpServletRequest request, Model model) throws ServletException, IOException {
        log.debug("Command starts");

        String username = request.getParameter("username");
        log.trace("Get parameter from the request: username -->" + username);

        String password = request.getParameter("password");
        log.trace("Get parameter from the request: password -->" + password);

            if (userService.isCorrectAdmin(username, password)) {
                log.info("User with username " + username + "is admin");

                HttpSession session = request.getSession();
                session.setAttribute("username_admin", username);
                log.trace("Set attribute to the session: username -->" + username);

                log.debug("Command finished");
                return "redirect:/admin/list";
            }
        model.addAttribute("message", "Account's Invalid");
        log.trace("Set attribute to the request: message -->" + "Account's Invalid");

        log.debug("Command finished");
        return "login-admin";
    }
}
