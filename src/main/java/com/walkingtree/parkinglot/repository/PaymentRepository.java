package com.walkingtree.parkinglot.repository;

import com.walkingtree.parkinglot.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}