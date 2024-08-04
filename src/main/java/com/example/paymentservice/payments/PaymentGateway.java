package com.example.paymentservice.payments;

import com.example.paymentservice.models.order.Order;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import org.springframework.security.oauth2.jwt.Jwt;

public interface PaymentGateway {
    public String generatePaymentLink(Order order, Jwt jwt) throws RazorpayException, StripeException;
}
