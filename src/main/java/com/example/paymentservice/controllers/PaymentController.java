package com.example.paymentservice.controllers;

import com.example.paymentservice.dtos.GeneratePaymentLinkDTO;
import com.example.paymentservice.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path="/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/")
    public ResponseEntity<String> generatePaymentLink(@RequestBody GeneratePaymentLinkDTO dto){
        try{
        String link=paymentService.generatePaymentLinkService(dto.getOrderId(), dto.getAmount());
        return ResponseEntity.ok(link);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }
}
