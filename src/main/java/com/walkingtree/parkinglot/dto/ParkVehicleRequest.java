package com.walkingtree.parkinglot.dto;

import com.walkingtree.parkinglot.enums.VehicleType;
import lombok.Data;

@Data
public class ParkVehicleRequest {
    private String plateNo;
    private VehicleType vehicleType;

    public ParkVehicleRequest(String plateNo, VehicleType vehicleType) {
        this.plateNo = plateNo;
        this.vehicleType = vehicleType;
    }
}