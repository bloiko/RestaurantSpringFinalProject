package com.restaurant.messaging.email;

import com.restaurant.database.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEmailDto implements Serializable {

        private String email;

        private Long orderId;

        private List<Item> orderItems;

        private BigDecimal orderFullPrice;

        private Timestamp orderDate;
}
