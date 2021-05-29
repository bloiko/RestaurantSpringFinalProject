package  com.restaurant.controller;

import lombok.extern.slf4j.Slf4j;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
/**
 * Logout controller.
 * This controller simply delete all session data about user and simulate logout from the system.
 *
 * @author B.Loiko
 *
 */
@Slf4j
@WebServlet("/LogoutController")
public class LogoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Controller starts");
        HttpSession session = request.getSession();
        session.removeAttribute("username");
        log.trace("Session atribute was removed : username");
        session.removeAttribute("cart");
        log.trace("Session atribute was removed : cart");

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/FoodItemController");
        requestDispatcher.forward(request, response);
        log.debug("Controller finished");
    }


}
