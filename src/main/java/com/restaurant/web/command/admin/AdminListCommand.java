package com.restaurant.web.command.admin;


import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.OrderStatus;
import com.restaurant.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Command that shows list of orders in admin page.
 *
 * @author B.Loiko
 */
@Slf4j
@Controller
public class AdminListCommand {
    @Autowired
    private OrderService orderService;

    @GetMapping("/admin/list")
    public String execute( Model model) throws ServletException, IOException {
        log.debug("Command starts");
        List<OrderStatus> orderStatuses = orderService.getStatuses();
        log.trace("Get statuses from Service : statuses --> " + orderStatuses);

        List<Order> notDoneOrders = orderService.getNotDoneOrdersSortByIdDesc();
       log.trace("Get not done orders from Service : notDoneOrders --> " + notDoneOrders);

        List<Order> doneOrders = orderService.getDoneOrders();
       log.trace("Get done orders from Service : doneOrders --> " + doneOrders);

        model.addAttribute("statusList", orderStatuses);
        log.trace("Set request parameter: statusList" + orderStatuses);

        model.addAttribute("NOT_DONE_ORDERS_LIST", notDoneOrders);
        log.trace("Set request parameter: NOT_DONE_ORDERS_LIST" + notDoneOrders);

        model.addAttribute("DONE_ORDERS_LIST", doneOrders);
        log.trace("Set request parameter: DONE_ORDERS_LIST" + doneOrders);
        log.debug("Command finished");
        return "admin";
    }
}
