package com.restaurant.web.command.cart;

import com.restaurant.database.entity.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Command that shows cart list in the cart page.
 *
 * @author B.Loiko
 */
@Slf4j
@Controller
public class CartListCommand  {

    @GetMapping("/cart")
    public String execute(HttpServletRequest request, Model model)  {
        log.debug("Command starts");
        HttpSession session = request.getSession();
        List<Item> items = (List<Item>) session.getAttribute("cart");
        if(items==null){
            items=new ArrayList<>();
        }
        int sum = 0;
        for (Item item : items){
            sum +=item.getFoodItem().getPrice()*item.getQuantity();
        }
        model.addAttribute("sum",sum);
        log.debug("Command finished");
        return "cart";
    }

}
