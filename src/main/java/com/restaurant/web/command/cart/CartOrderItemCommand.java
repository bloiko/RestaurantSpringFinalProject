package com.restaurant.web.command.cart;

import com.restaurant.database.entity.Item;
import com.restaurant.database.entity.User;
import com.restaurant.service.OrderService;
import com.restaurant.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
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
@Controller
public class CartOrderItemCommand {
    private final UserService userService;
    private final OrderService orderService;

    public CartOrderItemCommand(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/cart/order")
    public String execute(HttpServletRequest request, Model model) throws IOException, ServletException {
        log.debug("Command starts");
        HttpSession session = request.getSession();
        String username = request.getUserPrincipal().getName();
        log.trace("Get attribute: username --> " + username);
        User user;
        if (username == null) {
            session.setAttribute("command", "ORDER_IN_CART");
            log.trace("Set attribute to the session: command --> " + "ORDER_IN_CART");
            log.debug("Command finished");
            return "login-main";
        } else {
            user = userService.getUserByUserName(username);
            log.info("User with username " + username + " was taken from the database");
        }

        List<Item> cart = (List<Item>) session.getAttribute("cart");
        Long orderId = orderService.addOrderAndGetId(cart, user);
        model.addAttribute("orderId", orderId);
        log.trace("Set attribute to the request: orderId --> " + orderId);

        session.setAttribute("cart", new ArrayList<Item>());
        log.trace("Set attribute to the request: cart --> new list with 0 size");

        log.debug("Command finished");
        return "thanks-page";
    }

}
