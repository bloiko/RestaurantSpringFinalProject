package  com.restaurant.controller;

import com.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Login Main controller.
 * This login is used main part of the program. Also, after registration.
 *
 * @author B.Loiko
 */
@WebServlet("/LoginMainController")
public class LoginMainController extends HttpServlet {
    public static final String COMMAND = "web/command";
    @Autowired
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(COMMAND, request.getAttribute(COMMAND));
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("login-main.html");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        removePastErrorMessagesIfExist(session);
        String username = request.getParameter("username");
        String password = request.getParameter("password");
            if (userService.isCorrectUser(username, password)) {
                session.setAttribute("username", username);
                if ("ORDER_IN_CART".equals(session.getAttribute(COMMAND))) {
                    session.removeAttribute(COMMAND);
                    response.sendRedirect("/CartController");
                } else {
                    response.sendRedirect("/FoodItemController");
                }
            } else {
                session.setAttribute("message", "Account's Invalid");
                response.sendRedirect("/LoginMainController");
            }
    }

    private void removePastErrorMessagesIfExist(HttpSession session) {
        String pastMessage = (String) session.getAttribute("message");
        if (pastMessage != null) {
            session.removeAttribute("message");
        }
    }
}
