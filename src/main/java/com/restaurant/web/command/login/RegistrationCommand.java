package com.restaurant.web.command.login;

import com.restaurant.database.entity.Role;
import com.restaurant.database.entity.User;
import com.restaurant.service.UserService;
import com.restaurant.web.command.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Command process data from the registration.
 *
 * @author B.Loiko
 */
@Slf4j
@Controller
@RequestMapping("/registration")
public class RegistrationCommand {
    public static final String PASSWORD = "password";
    public static final String USERNAME = "username";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String ADDRESS = "address";
    public static final String EMAIL = "email";
    public static final String LAST_NAME = "last_name";
    public static final String FIRST_NAME = "first_name";
    @Autowired
    private UserService userService;

    @GetMapping
    public String getRegistrationPage(){
        return "registration";
    }
    @PostMapping
    public String execute(HttpServletRequest request, Model model) throws ServletException, IOException {
        log.debug("Command starts");
        User user = getUserIfCorrectData(request);
        if (user == null) {
            log.info("User data is invalid");
            prepareDataToTheRedirection(request,model);
            log.trace("User data was prepared to the redirection");

            log.debug("Command finished");
            return "registration";
        } else {
                userService.addUserAndReturnId(user);
                log.info("User was added to the database");
            log.debug("Command finished");
            return "login-main";
        }
    }

    private void prepareDataToTheRedirection(HttpServletRequest request, Model model) {
        String firstName = request.getParameter(FIRST_NAME);
        model.addAttribute(FIRST_NAME, firstName);
        log.trace("Set parameter to the request: FIRST_NAME --> " + firstName);

        String lastName = request.getParameter(LAST_NAME);
        model.addAttribute(LAST_NAME, lastName);
        log.trace("Set parameter to the request: LAST_NAME --> " + lastName);

        String email = request.getParameter(EMAIL);
        model.addAttribute(EMAIL, email);
        log.trace("Set parameter to the request: EMAIL --> " + email);

        String address = request.getParameter(ADDRESS);
        model.addAttribute(ADDRESS, address);
        log.trace("Set parameter to the request: ADDRESS --> " + address);

        String phoneNumber = request.getParameter(PHONE_NUMBER);
        model.addAttribute(PHONE_NUMBER, phoneNumber);
        log.trace("Set parameter to the request: PHONE_NUMBER --> " + phoneNumber);


        String username = request.getParameter(USERNAME);
        model.addAttribute(USERNAME, username);
        log.trace("Set parameter to the request: USERNAME --> " + username);

        String password = request.getParameter(PASSWORD);
        model.addAttribute(PASSWORD, password);
        log.trace("Set parameter to the request: PASSWORD --> " + password);

        model.addAttribute("command", "REDIRECT");
        log.trace("Set attribute to the request: command --> " + "REDIRECT");

    }

    private User getUserIfCorrectData(HttpServletRequest request) {
        log.debug("Check data on correctness");
        boolean isCorrect = true;
        String firstName = request.getParameter(FIRST_NAME);
        String lastName = request.getParameter(LAST_NAME);
        String email = request.getParameter(EMAIL);
        String address = request.getParameter(ADDRESS);
        String phoneNumber = request.getParameter(PHONE_NUMBER);
        String username = request.getParameter(USERNAME);
        String password = request.getParameter(PASSWORD);
        HttpSession session = request.getSession();
        session.removeAttribute("error_message");
        session.removeAttribute("password_error_message");
        session.removeAttribute("username_error_message");
        session.removeAttribute("phone_number_error_message");
        session.removeAttribute("email_error_message");
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || username.isEmpty() || password.isEmpty()) {
            session.setAttribute("error_message", "Everything must be filled!");
            isCorrect = false;
        }
            if (username != null && userService.getUserByUserName(username) != null) {//if user exists
                session.setAttribute("username_error_message", "Username should be unique!");
                isCorrect = false;
            }
        if (password.length() < 8) {
            session.setAttribute("password_error_message", "Password should has at least 8 symbols!");
            isCorrect = false;
        }

        if (!userService.isCorrectPhoneNumber(phoneNumber)) {
            session.setAttribute("phone_number_error_message", "Incorrect phone number format!");
            isCorrect = false;
        }
        if (!userService.isCorrectEmail(email)) {
            session.setAttribute("email_error_message", "Incorrect email format!");
            isCorrect = false;
        }
        log.debug("Data was checked with result: " + isCorrect);
        if (isCorrect) {
            return new User(0L, firstName, lastName, username, password, email, address, phoneNumber, new Role(2L,"USER"));
        } else {
            return null;
        }
    }
}

