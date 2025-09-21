package com.walkingtree.parkinglot.model;

import com.walkingtree.parkinglot.enums.VehicleType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PricingRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private VehicleType vehicleType;

    private double basePrice;
    private double hourlyRate;
}