package com.restaurant.controller;


import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.OrderStatus;
import com.restaurant.service.OrderService;
import com.restaurant.service.OrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Admin controller.
 *
 * @author B.Loiko
 */
@WebServlet("/AdminController")
public class AdminController extends HttpServlet {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderStatusService statusService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<OrderStatus> orderStatuses = orderService.getStatuses();
        List<Order> notDoneOrders = orderService.getNotDoneOrdersSortByIdDesc();
        List<Order> doneOrders = orderService.getDoneOrders();

        request.setAttribute("statusList", orderStatuses);
        request.setAttribute("NOT_DONE_ORDERS_LIST", notDoneOrders);
        request.setAttribute("DONE_ORDERS_LIST", doneOrders);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("admin.html");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String statusName = request.getParameter("status");
        String orderIdString = request.getParameter("orderId");
        Order order;
        try {
            order = orderService.getOrder(orderIdString);
            OrderStatus newStatus = statusService.findByStatusName(statusName);
            orderService.updateOrder(order.getId(), newStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.doGet(request, response);
    }
}
