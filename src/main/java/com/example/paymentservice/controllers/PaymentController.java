package com.example.paymentservice.controllers;

import com.example.paymentservice.dtos.GeneratePaymentLinkDTO;
import com.example.paymentservice.dtos.SendOrderConfirmation;
import com.example.paymentservice.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/payment")
public class PaymentController {

    private final PaymentService paymentService;
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/")
    public ResponseEntity<String> generatePaymentLink(@RequestBody GeneratePaymentLinkDTO dto, @AuthenticationPrincipal Jwt jwt){

        try{
        String link=paymentService.generatePaymentLinkService(dto.getOrderId(), (long) dto.getAmount(), jwt);
        return ResponseEntity.ok(link);

        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> afterSuccessPayment(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt){
        System.out.println(jwt);
        return ResponseEntity.ok(paymentService.afterSuccess(id));
    }

    @PostMapping("/order")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> afterSuccessPaymentOrder(@RequestBody SendOrderConfirmation sendOrderConfirmation, @AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok(paymentService.orderConfirmation(sendOrderConfirmation.getOrderId()));
    }

}
