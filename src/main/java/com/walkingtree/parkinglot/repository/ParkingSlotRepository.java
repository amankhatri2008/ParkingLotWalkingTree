package com.walkingtree.parkinglot.repository;

import com.walkingtree.parkinglot.enums.SlotStatus;
import com.walkingtree.parkinglot.enums.VehicleType;
import com.walkingtree.parkinglot.model.ParkingSlot;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from ParkingSlot s where s.id = :id")
    Optional<ParkingSlot> findByIdWithLock(@Param("id") Long id);

    long countByStatus(SlotStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM ParkingSlot s WHERE s.status = :status AND s.slotType >= :vehicleType ORDER BY s.id ASC LIMIT 1")
    Optional<ParkingSlot> findFirstAvailableSlot(
            @Param("status") SlotStatus status,
            @Param("vehicleType") VehicleType vehicleType);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM ParkingSlot s WHERE s.status = :status AND s.slotType >= :vehicleType ORDER BY s.floor ASC, s.id ASC")
    List<ParkingSlot> findAllAvailableSlotsLevelWise(
            @Param("status") SlotStatus status,
            @Param("vehicleType") VehicleType vehicleType);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM ParkingSlot s WHERE s.status = :status AND s.slotType >= :vehicleType ORDER BY s.id ASC")
    List<ParkingSlot> findAllAvailableSlotsWithLock(
            @Param("status") SlotStatus status,
            @Param("vehicleType") VehicleType vehicleType);
}