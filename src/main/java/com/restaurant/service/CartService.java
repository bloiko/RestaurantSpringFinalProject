package com.restaurant.service;

import com.restaurant.database.entity.Item;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Cart service.
 *
 * @author B.Loiko
 */
@Service
public class CartService {

    public List<Item> removeItemFromCart(List<Item> cart, String itemId) {
        int index = isExisting(Integer.parseInt(itemId), cart);
        if (index != -1) {
            cart.remove(index);
        }
        return cart;
    }

    public int isExisting(int id, List<Item> cart) {
        Optional<Item> optionalItem = cart.stream()
                .filter(a -> a.getFoodItem().getId() == id)
                .findAny();
        return optionalItem.isPresent() ? cart.indexOf(optionalItem.get()) : -1;
    }
}
