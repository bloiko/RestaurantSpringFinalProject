package com.restaurant.web.command.menu;


import com.restaurant.database.entity.Item;
import com.restaurant.service.FoodItemService;
import com.restaurant.web.command.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

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
public class MenuOrderCommand extends Command {
    @Autowired
    private FoodItemService foodItemService;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        List<Item> cart = getCart(session);
        String foodId = request.getParameter("foodId");
        foodItemService.addFoodItemToCart(cart, foodId);
        session.setAttribute("cart", cart);
        return "/controller?command=menuList";
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













