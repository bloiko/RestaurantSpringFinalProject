package com.restaurant.web.command.menu;


import com.restaurant.database.entity.Item;
import com.restaurant.service.FoodItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Command that order item to the cart from menu list.
 *
 * @author B.Loiko
 */
@Slf4j
@Controller
public class MenuOrderCommand {
    @Autowired
    private FoodItemService foodItemService;

    @GetMapping("/menu/order")
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        List<Item> cart = getCart(session);
        String foodId = request.getParameter("foodId");
        foodItemService.addFoodItemToCart(cart, foodId);
        session.setAttribute("cart", cart);
        return "redirect:/menu";
    }

    private List<Item> getCart(HttpSession session) {
        List<Item> cart = (List<Item>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

}













