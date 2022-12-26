package com.restaurant.service;

import com.restaurant.RestaurantApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(classes = RestaurantApplication.class)
@TestPropertySource(locations="classpath:application-test.properties")
class OrderServiceIT {


    @Test
    void getStatuses() {
    }

    @Test
    void getDoneOrders() {
    }

    @Test
    void getNotDoneOrdersSortByIdDesc() {
    }

    @Test
    void getOrder() {
    }

    @Test
    void updateOrder() {
    }
}