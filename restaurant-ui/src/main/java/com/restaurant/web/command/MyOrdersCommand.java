package com.restaurant.web.command;


import com.restaurant.database.entity.Order;
import com.restaurant.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


/**
 * Command that shows list of all user's orders.
 *
 * @author B.Loiko
 */

@Deprecated
@Slf4j
@Controller
public class MyOrdersCommand {
    private final UserService userService;

    public MyOrdersCommand(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/myorders")
    public String execute(HttpServletRequest request) throws IOException, ServletException {
        log.debug("Controller starts");
        String username = request.getUserPrincipal().getName();
        log.trace("Session attribute : username" + username);

        List<Order> orders = getOrdersByUsername(username);

        request.setAttribute("ORDERS_LIST", orders);
        log.trace("Session attribute : username" + username);

        log.debug("Controller finished");
        return "my-orders";
    }

    private List<Order> getOrdersByUsername(String username) {
        List<Order> orders = new LinkedList<>();
        if (username != null) {
            orders = userService.getUserOrdersSortByOrderDateReversed(username);
        }
        return orders;
    }
}
