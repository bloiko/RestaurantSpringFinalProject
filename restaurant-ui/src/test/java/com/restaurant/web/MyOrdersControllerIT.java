package com.restaurant.web;

import com.restaurant.RestaurantApplication;
import com.restaurant.web.dto.OrdersDto;
import com.restaurant.web.dto.OrdersResponse;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.core.userdetails.User.withUsername;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(classes = RestaurantApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql({"classpath:db-test/test-food-orders.sql"})
class MyOrdersControllerIT {

    private static final String USER_NAME = "usernameTest";
    @Autowired
    private MyOrdersController myOrdersController;

    @Test
    public void testGetMenuPage() {
        setSecurityContext();

        OrdersResponse ordersResponse = myOrdersController.getMyOrders();

        assertNotNull(ordersResponse);
        List<OrdersDto> orders = ordersResponse.getOrders();
        assertTrue(orders.size() > 4);
        orders.forEach(orderDto -> {
            assertNotNull(orderDto.getId());
            assertNotNull(orderDto.getOrderDate());
            assertTrue(orderDto.getOrderPrice() > 0);
            assertTrue(orderDto.getFoodItems().size() > 0);
            assertNotNull(orderDto.getOrderStatus());
        });
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