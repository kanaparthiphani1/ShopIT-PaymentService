package com.example.paymentservice.services;

import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import org.springframework.security.oauth2.jwt.Jwt;

public interface PaymentService {
    public String generatePaymentLinkService(Long orderId, Long amount, Jwt jwt) throws RazorpayException, StripeException;
    public String afterSuccess(Long orderId);
    public String orderConfirmation(Long orderId);
}
