package com.restaurant.web;

import com.restaurant.RestaurantApplication;
import com.restaurant.database.dao.OrderRepository;
import com.restaurant.database.entity.Order;
import com.restaurant.database.entity.Status;
import com.restaurant.web.dto.OrdersDto;
import com.restaurant.web.dto.OrdersResponse;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(classes = RestaurantApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql({"classpath:db-test/test-food-orders.sql"})
class CookOrdersControllerIT {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CookOrdersController cookOrdersController;

    @Test
    void getOrdersForCook() {
        OrdersResponse ordersResponse = cookOrdersController.getOrdersForCook();

        assertNotNull(ordersResponse);
        List<OrdersDto> requestedOrders = ordersResponse.getOrders();
        assertTrue(requestedOrders.size() == 5);
        requestedOrders.forEach(ordersDto -> {
            assertTrue(Arrays.asList(Status.WAITING.name(), Status.PREPARING.name())
                    .contains(ordersDto.getOrderStatus()));
        });
        List<OrdersDto> sortedOrders = requestedOrders.stream()
                .sorted(Comparator.comparing(OrdersDto::getOrderDate))
                .collect(Collectors.toList());
        assertEquals(sortedOrders, requestedOrders);
    }

    @Test
    void changeOrderStatus() {
        Order order = orderRepository.getById(1L);
        assertEquals(Status.WAITING, order.getOrderStatus().getStatusName());

        boolean boolValue = cookOrdersController.changeOrderStatus(1L, Status.READY);

        assertTrue(boolValue);

        Order orderAfterChanging = orderRepository.getById(1L);
        assertEquals(Status.READY, orderAfterChanging.getOrderStatus().getStatusName());
    }

    @Test()
    void changeOrderStatusWithLowerStatus() {
        Order order = orderRepository.getById(8L);
        assertEquals(Status.DONE, order.getOrderStatus().getStatusName());


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cookOrdersController.changeOrderStatus(8L, Status.READY);
        });

        String expectedMessage = "Requested status cannot be less ordinal than current";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.equals(expectedMessage));
    }
}