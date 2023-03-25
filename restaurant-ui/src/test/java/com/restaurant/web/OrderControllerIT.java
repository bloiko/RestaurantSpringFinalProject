package com.restaurant.web;

import com.restaurant.RestaurantApplication;
import com.restaurant.database.dao.FoodRepository;
import com.restaurant.database.dao.OrderRepository;
import com.restaurant.database.entity.FoodItem;
import com.restaurant.database.entity.Item;
import com.restaurant.database.entity.Order;
import com.restaurant.web.dto.FoodItemDto;
import com.restaurant.web.dto.OrderRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.util.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.core.userdetails.User.withUsername;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(classes = RestaurantApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql({"classpath:db-test/test-user-data.sql", "classpath:db-test/test-food-data.sql"})
class OrderControllerIT {

    private static final String USER_NAME = "usernameTest";

    @Autowired
    private OrderController orderController;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void orderCart() {
        setSecurityContext();
        OrderRequest orderRequest = new OrderRequest(newArrayList(new FoodItemDto(1L, 2), new FoodItemDto(2L, 5)));

        Long orderId = orderController.orderCart(orderRequest);

        Order orderFromDb = orderRepository.getById(orderId);
        assertEquals(orderId, orderFromDb.getId());
        assertEquals(USER_NAME, orderFromDb.getUser().getUserName());
        FoodItem foodItem1 = foodRepository.getById(1L);
        FoodItem foodItem2 = foodRepository.getById(2L);
        List<Item> itemList = orderFromDb.getItems();
        assertEquals(2, itemList.size());
        assertEquals(foodItem1.getId(), itemList.get(0).getId());
        assertEquals(foodItem2.getId(), itemList.get(1).getId());
        assertEquals(foodItem1.getPrice() * 2L + foodItem2.getPrice() * 5L, orderFromDb.getOrderPrice().longValue());
        assertNotNull(orderFromDb.getOrderDate());
    }

    private static void setSecurityContext() {
        UserDetails userDetails = withUsername(USER_NAME)
                .authorities("USER")
                .password("")
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new PreAuthenticatedAuthenticationToken(userDetails, "", userDetails.getAuthorities()));
    }
}