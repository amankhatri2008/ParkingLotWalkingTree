package com.walkingtree.parkinglot.controller;

import com.walkingtree.parkinglot.dto.ParkingStatusResponse;
import com.walkingtree.parkinglot.model.ParkingSlot;
import com.walkingtree.parkinglot.model.PricingRule;
import com.walkingtree.parkinglot.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/slots")
    public ResponseEntity<ParkingSlot> addSlot(@RequestBody ParkingSlot slot) {
        return ResponseEntity.ok(adminService.addSlot(slot));
    }

    @DeleteMapping("/slots/{id}")
    public ResponseEntity<Void> removeSlot(@PathVariable Long id) {
        adminService.removeSlot(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/pricing-rules")
    public ResponseEntity<PricingRule> addPricingRule(@RequestBody PricingRule rule) {
        return ResponseEntity.ok(adminService.addPricingRule(rule));
    }

    @GetMapping("/status")
    public ResponseEntity<ParkingStatusResponse> getParkingStatus() {
        return ResponseEntity.ok(adminService.getParkingStatus());
    }
}