package com.restaurant.web.command.cart;

import com.restaurant.database.entity.Item;
import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.User;
import com.restaurant.service.OrderService;
import com.restaurant.service.UserService;
import com.restaurant.web.command.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Command that orders item to the cart
 *
 * @author B.Loiko
 */
@Slf4j
public class CartOrderItemCommand extends Command {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("Command starts");
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        log.trace("Set attribute to the session: username --> " + username);

        User user;
        if (username == null) {
            session.setAttribute("command", "ORDER_IN_CART");
            log.trace("Set attribute to the session: command --> " + "ORDER_IN_CART");

            log.debug("Command finished");
            return "login-main.html";
        } else {
            user = userService.getUserByUserName(username);
            log.info("User with username " + username + " was taken from the database");
        }

        List<Item> cart = (List<Item>) session.getAttribute("cart");
        Long orderId = orderService.addOrderAndGetId(cart, user);
        request.setAttribute("orderId", orderId);
        log.trace("Set attribute to the request: orderId --> " + orderId);

        session.setAttribute("cart", new ArrayList<Order>());
        log.trace("Set attribute to the request: cart --> new list with 0 size");

        log.debug("Command finished");
        return "thanks-page.html";
    }

}
