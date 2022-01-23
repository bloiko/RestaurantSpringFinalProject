package com.restaurant.web.command;


import com.restaurant.database.entity.Order;
import com.restaurant.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


/**
 * Command that shows list of all user's orders.
 *
 * @author B.Loiko
 *
 */
@Slf4j
@Controller
public class MyOrdersCommand {
    @Autowired
    private UserService userService;

    @GetMapping("/myorders")
    public String execute(HttpServletRequest request) throws IOException, ServletException {
        log.debug("Controller starts");
        String username = request.getUserPrincipal().getName();
        log.trace("Session attribute : username" + username);

        List<Order> orders = new LinkedList<>();
        if (username != null) {
                orders = userService.getUserOrdersSortByOrderDateReversed(username);
        }
        request.setAttribute("ORDERS_LIST", orders);
        log.trace("Session attribute : username" + username);

        log.debug("Controller finished");
        return "my-orders";
    }
}
