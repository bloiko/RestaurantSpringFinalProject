package com.restaurant.web.command.cart;

import com.restaurant.database.entity.Item;
import com.restaurant.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Command that deletes item from the cart in cart page.
 *
 * @author B.Loiko
 *
 */
@Slf4j
@Controller
public class CartDeleteItemCommand  {
    @Autowired
    private CartService cartService;

    @GetMapping("/cart/delete")
    public String execute(@RequestParam String itemId, HttpSession session) throws IOException, ServletException {
        log.debug("Command starts");

        List<Item> cart = (List<Item>) session.getAttribute("cart");
        log.trace("Get attribute from the session: cart -->"+cart);

        cart = cartService.removeItemFromCart(cart, itemId);
        log.trace("Remove item "+itemId+" from the cart using CartService");

        session.setAttribute("cart", cart);
        log.trace("Set attribute to the request: cart -->"+cart);

        log.debug("Command finished");
        return "redirect:/cart";
    }

}
