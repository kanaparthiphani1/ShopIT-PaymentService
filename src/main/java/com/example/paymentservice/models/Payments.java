package com.example.paymentservice.models;

import com.example.paymentservice.utils.PaymnetStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Payments extends BaseModel{
    private Long orderId;
    private double amount;
    @Enumerated(EnumType.STRING)
    private PaymnetStatus status;
}
