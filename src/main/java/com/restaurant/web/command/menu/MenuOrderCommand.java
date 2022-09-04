package com.restaurant.web.command.menu;


import com.restaurant.database.entity.Item;
import com.restaurant.service.FoodItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
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
    private final FoodItemService foodItemService;

    public MenuOrderCommand(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    @GetMapping("/menu/order")
    public String execute(@RequestParam String foodId, HttpSession session) {
        List<Item> cart = getCart(session);
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













