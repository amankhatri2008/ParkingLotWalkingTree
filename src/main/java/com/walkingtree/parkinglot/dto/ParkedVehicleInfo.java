package com.walkingtree.parkinglot.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ParkedVehicleInfo {
    private Long ticketId;
    private String plateNo;
    private Long slotId;
    private LocalDateTime entryTime;
}