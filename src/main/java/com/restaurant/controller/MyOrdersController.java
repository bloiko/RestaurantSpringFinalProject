package  com.restaurant.controller;

import com.restaurant.database.entity.Order;
import com.restaurant.exception.DBException;
import com.restaurant.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * My Orders controller.
 * This controller show to the user his orders
 *
 * @author B.Loiko
 *
 */
@Slf4j
@WebServlet("/MyOrdersController")
public class MyOrdersController extends HttpServlet {
    @Autowired
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Controller starts");
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        log.trace("Session atribute : username"+ username);

        List<Order> orders = new LinkedList<>();
        if(username!=null) {
                orders = userService.getUserOrdersSortByOrderDateReversed(username);
        }
        request.setAttribute("ORDERS_LIST", orders);
        log.trace("Session atribute : username"+ username);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("my-orders.html");
        requestDispatcher.forward(request, response);
        log.debug("Controller finished");
    }

}
