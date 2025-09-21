package com.walkingtree.parkinglot.repository;

import com.walkingtree.parkinglot.enums.VehicleType;
import com.walkingtree.parkinglot.model.PricingRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PricingRuleRepository extends JpaRepository<PricingRule, Long> {

    Optional<PricingRule> findByVehicleType(VehicleType vehicleType);
}