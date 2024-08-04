package com.example.paymentservice.repository;

import com.example.paymentservice.models.Payments;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payments, Long> {
    @NotNull
    Payments save(@NotNull Payments payments);

    Optional<Payments> findPaymentsByOrderId(Long id);
}
