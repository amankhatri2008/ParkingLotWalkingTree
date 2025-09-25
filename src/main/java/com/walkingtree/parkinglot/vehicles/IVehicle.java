package com.walkingtree.parkinglot.vehicles;

import com.walkingtree.parkinglot.enums.VehicleType;

import java.util.Arrays;
import java.util.List;

import static com.walkingtree.parkinglot.enums.VehicleType.CAR;

public interface IVehicle {

    public static IVehicle createVehicle(VehicleType vehicleTypeEnum) {

        switch (vehicleTypeEnum) {
            case BIKE:
                return new Bike();
            case CAR:
                return new Car();
            case TRUCK:
                return new Truck();
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + vehicleTypeEnum);
        }
    }

    public static List<String> getCompatibleSlotTypes(String vehicleType) {
        if (VehicleType.CAR.name().equals(vehicleType)) {
            return Arrays.asList("CAR", "TRUCK"); // A car can fit in a car or a truck slot
        } else if (VehicleType.BIKE.name().equals(vehicleType)) {
            return Arrays.asList("BIKE", "CAR"); // A bike can fit in a bike or a car slot
        } else if (VehicleType.TRUCK.name().equals(vehicleType)) {
            return Arrays.asList("TRUCK"); // A truck only fits in a truck slot
        }
        return Arrays.asList();
    }
}
