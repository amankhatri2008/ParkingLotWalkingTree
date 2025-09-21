package com.walkingtree.parkinglot.model;

import com.walkingtree.parkinglot.enums.SlotStatus;
import com.walkingtree.parkinglot.enums.VehicleType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ParkingSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private VehicleType slotType;

    private Long floor;

    @Enumerated(EnumType.STRING)
    private SlotStatus status;

    public ParkingSlot() {
    }

    public ParkingSlot(long id, VehicleType vehicleType, Long floor, SlotStatus status) {
        this.id = id;
        this.slotType = vehicleType;
        this.floor = floor;
        this.status = status;
    }
}