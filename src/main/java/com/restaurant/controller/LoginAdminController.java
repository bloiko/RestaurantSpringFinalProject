package com.restaurant.controller;

import com.restaurant.service.OrderService;
import com.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Login Admin controller.
 * This login is used in AdminFilter that goes before showing admin.html
 *
 * @author B.Loiko
 */
@WebServlet("/LoginAdminController")
public class LoginAdminController extends HttpServlet {
    @Autowired
    private UserService userService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (userService.isCorrectAdmin(username, password)) {
            HttpSession session = request.getSession();
            session.setAttribute("username_admin", username);
            request.getRequestDispatcher("/AdminController").forward(request, response);
        } else {
            request.setAttribute("message", "Account's Invalid");
            request.getRequestDispatcher("login-admin.html").forward(request, response);
        }
    }
}
