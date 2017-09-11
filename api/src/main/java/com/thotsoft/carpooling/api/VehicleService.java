package com.thotsoft.carpooling.api;

import com.thotsoft.carpooling.api.model.Vehicle;

import java.util.List;

public interface VehicleService extends CarpoolingService {

    void addVehicle(Vehicle vehicle);

    boolean removeVehicle(String licenceNumber);

    boolean removeVehicle(Vehicle vehicle);

    Vehicle getVehicle(String licenceNumber);

    void updateVehicle(String licenceNumber, Vehicle vehicle);

    List<Vehicle> listVehicles();
}
