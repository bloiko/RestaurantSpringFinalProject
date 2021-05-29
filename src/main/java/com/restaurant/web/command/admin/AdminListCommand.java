package com.restaurant.web.command.admin;


import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.OrderStatus;
import com.restaurant.service.OrderService;
import com.restaurant.web.command.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Command that shows list of orders in admin page.
 *
 * @author B.Loiko
 *
 */
@Slf4j
public class AdminListCommand extends Command {
    @Autowired
    private OrderService orderService;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Command starts");
            List<OrderStatus> orderStatuses = orderService.getStatuses();
            log.trace("Get statuses from Service : statuses --> " + orderStatuses);

            List<Order> notDoneOrders = orderService.getNotDoneOrdersSortById();
            log.trace("Get not done orders from Service : notDoneOrders --> " + notDoneOrders);

            List<Order> doneOrders = orderService.getDoneOrders();
            log.trace("Get done orders from Service : doneOrders --> " + doneOrders);

            request.setAttribute("statusList", orderStatuses);
            log.trace("Set request parameter: statusList"+orderStatuses);

            request.setAttribute("NOT_DONE_ORDERS_LIST", notDoneOrders);
            log.trace("Set request parameter: NOT_DONE_ORDERS_LIST"+notDoneOrders);

            request.setAttribute("DONE_ORDERS_LIST", doneOrders);
            log.trace("Set request parameter: DONE_ORDERS_LIST"+doneOrders);
        log.debug("Command finished");
        return  "admin.html";
    }
}
