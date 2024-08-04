package com.example.paymentservice.dtos;

import com.example.paymentservice.models.order.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SendOrderConfirmation {
    private Long orderId;
    private PaymentStatus paymentStatus;
}
