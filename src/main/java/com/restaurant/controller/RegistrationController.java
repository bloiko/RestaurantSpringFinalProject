package com.restaurant.controller;

import com.restaurant.database.entity.Role;
import com.restaurant.database.entity.User;
import com.restaurant.exception.DBException;
import com.restaurant.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Registration controller.
 * This controller show registration page and proccess dota from the registration form.
 *
 * @author B.Loiko
 */
@WebServlet("/RegistrationController")
public class RegistrationController extends HttpServlet {
    public static final String PASSWORD = "password";
    public static final String USERNAME = "username";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String ADDRESS = "address";
    public static final String EMAIL = "email";
    public static final String LAST_NAME = "last_name";
    public static final String FIRST_NAME = "first_name";
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("registration.html");
        requestDispatcher.include(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = getUserIfCorrectData(request);
        if (user == null) {
            request.setAttribute(FIRST_NAME, request.getParameter(FIRST_NAME));
            request.setAttribute(LAST_NAME, request.getParameter(LAST_NAME));
            request.setAttribute(EMAIL, request.getParameter(EMAIL));
            request.setAttribute(ADDRESS, request.getParameter(ADDRESS));
            request.setAttribute(PHONE_NUMBER, request.getParameter(PHONE_NUMBER));
            request.setAttribute(USERNAME, request.getParameter(USERNAME));
            request.setAttribute(PASSWORD, request.getParameter(PASSWORD));
            request.setAttribute("command", "REDIRECT");
            doGet(request, response);
        } else {
            userService.addUserAndReturnId(user);
            response.sendRedirect("login-main.html");
        }
    }

    private User getUserIfCorrectData(HttpServletRequest request) {
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
        if (isCorrect) {
            return new User(0L, firstName, lastName, username, password, email, address, phoneNumber, new Role(2L,"User"));
        } else {
            return null;
        }
    }
}

