package com.example.paymentservice.models.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseModel{

    private List<OrderItems> orderItemsList;

    private Long userId;

    private double price;

    private OrderStatus status;

    private PaymentStatus paymentStatus;

}
