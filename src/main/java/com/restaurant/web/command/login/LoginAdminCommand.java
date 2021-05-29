package com.restaurant.web.command.login;

import com.restaurant.exception.DBException;
import com.restaurant.service.UserService;
import com.restaurant.web.command.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class LoginAdminCommand extends Command {
    @Autowired
    private UserService userService;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                return "/controller?command=adminList";
            }
        request.setAttribute("message", "Account's Invalid");
        log.trace("Set attribute to the request: message -->" + "Account's Invalid");

        log.debug("Command finished");
        return "login-admin.html";
    }
}
