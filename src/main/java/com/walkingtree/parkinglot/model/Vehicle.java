package com.walkingtree.parkinglot.model;

import com.walkingtree.parkinglot.enums.VehicleType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String plateNo;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
}