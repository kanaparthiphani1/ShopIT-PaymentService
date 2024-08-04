package com.example.paymentservice.services;

import com.example.paymentservice.models.Payments;
import com.example.paymentservice.models.order.Order;
import com.example.paymentservice.payments.PaymentGateway;
import com.example.paymentservice.repository.PaymentRepository;
import com.example.paymentservice.services.client.OrderFeignClient;
import com.example.paymentservice.utils.PaymnetStatus;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService{

    private PaymentGateway gateway;
    private OrderFeignClient feignClient;
    private PaymentRepository paymentRepository;

    public PaymentServiceImpl(@Qualifier("stripe") PaymentGateway gateway, OrderFeignClient feignClient, PaymentRepository paymentRepository) {
        this.gateway = gateway;
        this.feignClient = feignClient;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public String generatePaymentLinkService(Long orderId, Long amount, Jwt jwt) throws RazorpayException, StripeException {
        Order order = feignClient.getOrderById(orderId).getBody();
        Payments payments = new Payments();
        assert order != null;
        payments.setAmount(order.getPrice());
        payments.setOrderId(order.getId());
        payments.setStatus(PaymnetStatus.INITIATED);
        paymentRepository.save(payments);

        return gateway.generatePaymentLink(order,jwt);
    }

    @Override
    public String afterSuccess(Long orderId) {
        Optional<Payments> p = paymentRepository.findPaymentsByOrderId(orderId);
        if(p.isEmpty()) {
            return "Payment not found";
        }
        Payments payments = p.get();
        payments.setStatus(PaymnetStatus.SUCCESSFULL);
        paymentRepository.save(payments);
        System.out.println("Payment successful");

//        this.streamBridge.send("sendEmail-out-0",new SendEmailDto());
        return "Payment successfully";
    }

    @Override
    public String orderConfirmation(Long orderId) {
        feignClient.paymentSuccess(orderId);
        return "Sent Successfully";
    }
}
