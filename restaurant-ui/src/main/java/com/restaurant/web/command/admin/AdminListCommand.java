package com.restaurant.web.command.admin;


import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.OrderStatus;
import com.restaurant.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

/**
 * Command that shows list of orders in admin page.
 *
 * @author B.Loiko
 */

@Deprecated
@Slf4j
@Controller
public class AdminListCommand {
    private final OrderService orderService;

    public AdminListCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/admin/list")
    public String execute(Model model) throws ServletException, IOException {
        List<OrderStatus> orderStatuses = orderService.getStatuses();
        List<Order> notDoneOrders = orderService.getNotDoneOrdersSortByIdDesc();
        List<Order> doneOrders = orderService.getDoneOrders();

        model.addAttribute("statusList", orderStatuses);
        model.addAttribute("NOT_DONE_ORDERS_LIST", notDoneOrders);
        model.addAttribute("DONE_ORDERS_LIST", doneOrders);
        return "admin";
    }
}
