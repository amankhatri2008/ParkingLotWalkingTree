package com.walkingtree.parkinglot.repository;

import com.walkingtree.parkinglot.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    /**
     * Finds a Vehicle by its unique license plate number.
     *
     * @param plateNo The license plate number to search for.
     * @return An Optional containing the Vehicle if found, otherwise empty.
     */
    Optional<Vehicle> findByPlateNo(String plateNo);
}