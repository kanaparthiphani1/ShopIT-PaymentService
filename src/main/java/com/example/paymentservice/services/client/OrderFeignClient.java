package com.example.paymentservice.services.client;

import com.example.paymentservice.configs.BearerAuthFeignConfig;
import com.example.paymentservice.models.order.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "orderservice",configuration = BearerAuthFeignConfig.class)
public interface OrderFeignClient {
    @GetMapping("/order/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable("id") Long orderId);

    @PutMapping("/order/payment/{id}")
    public ResponseEntity<String> paymentSuccess(@PathVariable Long id);

}
