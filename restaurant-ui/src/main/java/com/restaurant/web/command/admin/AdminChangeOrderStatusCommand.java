package com.restaurant.web.command.admin;


import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.OrderStatus;
import com.restaurant.service.OrderService;
import com.restaurant.service.OrderStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Command that change order status in admin page.
 *
 * @author B.Loiko
 */
@Slf4j
@Controller
public class AdminChangeOrderStatusCommand {
    private final OrderService orderService;
    private final OrderStatusService orderStatusService;

    public AdminChangeOrderStatusCommand(OrderService orderService, OrderStatusService orderStatusService) {
        this.orderService = orderService;
        this.orderStatusService = orderStatusService;
    }

    @GetMapping("/admin/change-order-status")
    public String execute(@RequestParam String status, @RequestParam String orderId) throws ServletException, IOException {
        log.debug("Command starts");
        Order order = orderService.getOrder(orderId);
        log.trace("Get from Service by id" + orderId + ": order --> " + order);
        OrderStatus newStatus = orderStatusService.findByStatusName(status);
        orderService.updateOrder(order.getId(), newStatus);
        log.debug("Order was updated");
        log.debug("Command finished");
        return "redirect:/admin/list";
    }
}
