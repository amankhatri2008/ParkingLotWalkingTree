package com.walkingtree.parkinglot.dto;

import lombok.Data;

import java.util.List;

@Data
public class ParkingStatusResponse {
    private long totalSlots;
    private long availableSlots;
    private long occupiedSlots;
    private List<ParkedVehicleInfo> parkedVehicles;
}