package com.example.paymentservice.services;

import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface PaymentService {
    public String generatePaymentLinkService(Long orderId,Long amount) throws RazorpayException, StripeException;
}
