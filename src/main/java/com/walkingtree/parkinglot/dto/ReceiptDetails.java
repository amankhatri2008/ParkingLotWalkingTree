package com.walkingtree.parkinglot.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReceiptDetails {
    private Long ticketId;
    private String plateNo;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double totalAmount;
    private long durationInMinutes;
}