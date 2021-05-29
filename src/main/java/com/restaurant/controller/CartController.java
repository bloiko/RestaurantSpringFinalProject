package com.restaurant.controller;


import com.restaurant.database.entity.Item;
import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.User;
import com.restaurant.service.CartService;
import com.restaurant.service.OrderService;
import com.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Cart controller.
 *
 * @author B.Loiko
 */
@WebServlet("/CartController")
public class CartController extends HttpServlet {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String theCommand = request.getParameter("web/command");
        HttpSession session = request.getSession();
        if ("DELETE".equals(theCommand)) {
            List<Item> cart = (List<Item>) session.getAttribute("cart");
            String itemId = request.getParameter("itemId");
            cart = cartService.removeItemFromCart(cart, itemId);
            session.setAttribute("cart", cart);
        } else if ("ORDER".equals(theCommand)) {
            String username = (String) session.getAttribute("username");
            User user = null;
            if (username != null) {
                user = userService.getUserByUserName(username);
            } else {
                session.setAttribute("command", "ORDER_IN_CART");
                request.getRequestDispatcher("login-main.html").forward(request, response);
                return;
            }
            List<Item> cart = (List<Item>) session.getAttribute("cart");
            Long orderId = orderService.addOrderAndGetId(cart, user);
            request.setAttribute("orderId", orderId);
            session.setAttribute("cart", new ArrayList<Order>());
            request.getRequestDispatcher("thanks-page.html").forward(request, response);
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("cart.html");
        requestDispatcher.forward(request, response);
    }
}
