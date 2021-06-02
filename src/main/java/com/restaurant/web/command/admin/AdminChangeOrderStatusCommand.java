package com.restaurant.web.command.admin;


import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.OrderStatus;
import com.restaurant.service.OrderService;
import com.restaurant.service.OrderStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Command that change order status in admin page.
 *
 * @author B.Loiko
 */
@Slf4j
@Controller
public class AdminChangeOrderStatusCommand {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderStatusService orderStatusService;

@GetMapping("/admin/change-order-status")
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Command starts");

        String statusName = request.getParameter("status");
        log.trace("Get request parameter: status" + statusName);

        String orderIdString = request.getParameter("orderId");
        log.trace("Get request parameter: orderId" + orderIdString);


        Order order = orderService.getOrder(orderIdString);
        log.trace("Get from Service by id" + orderIdString + ": order --> " + order);
        OrderStatus newStatus = orderStatusService.findByStatusName(statusName);
        orderService.updateOrder(order.getId(), newStatus);
        log.debug("Order was updated");
        log.debug("Command finished");
        return "redirect:/admin/list";
    }
}
