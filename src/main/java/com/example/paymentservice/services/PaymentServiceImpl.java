package com.example.paymentservice.services;

import com.example.paymentservice.payments.PaymentGateway;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService{

    private PaymentGateway gateway;

    PaymentServiceImpl(@Qualifier("stripe") PaymentGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public String generatePaymentLinkService(Long orderId, Long amount) throws RazorpayException, StripeException {
        return gateway.generatePaymentLink(orderId,amount);
    }
}
