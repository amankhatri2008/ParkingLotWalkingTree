package com.walkingtree.parkinglot.repository;

import com.walkingtree.parkinglot.enums.VehicleType;
import com.walkingtree.parkinglot.model.PricingRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class PricingRuleRepositoryTest {

    @Autowired
    private PricingRuleRepository pricingRuleRepository;

    @Test
    void findByVehicleType_shouldReturnRule_whenRuleExists() {

        Optional<PricingRule> result = pricingRuleRepository.findByVehicleType(VehicleType.CAR);

        assertTrue(result.isPresent());
        assertEquals(VehicleType.CAR, result.get().getVehicleType());
        assertEquals(2.0, result.get().getBasePrice());
    }

    @Test
    void findByVehicleType_shouldReturnEmpty_whenRuleDoesNotExist() {
        Optional<PricingRule> result = pricingRuleRepository.findByVehicleType(VehicleType.TRUCK);

        assertTrue(result.isPresent());
    }
}