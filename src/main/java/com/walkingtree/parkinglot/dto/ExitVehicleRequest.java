package com.walkingtree.parkinglot.dto;

import lombok.Data;

@Data
public class ExitVehicleRequest {
    private Long ticketId;

    public ExitVehicleRequest(Long ticketId) {
        this.ticketId = ticketId;
    }
}