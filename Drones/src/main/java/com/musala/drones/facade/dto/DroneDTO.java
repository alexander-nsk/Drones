package com.musala.drones.facade.dto;

import com.musala.drones.model.Drone;
import com.musala.drones.model.DroneModel;
import com.musala.drones.model.DroneState;

/**
 * Data transfer object for {@link Drone}.
 */
public class DroneDTO {

    private String serialNumber;
    private DroneModel droneModel;
    private int weightLimit;
    private int batteryCapacity;
    private DroneState droneState;

    public static DroneDTO fromDrone(final Drone drone) {
        final DroneDTO dto = new DroneDTO();
        dto.serialNumber = drone.getSerialNumber();
        dto.droneModel = drone.getDroneModel();
        dto.batteryCapacity = drone.getBatteryCapacity();
        dto.droneState = drone.getDroneState();
        dto.weightLimit = drone.getWeightLimit();
        return dto;
    }

    public static Drone toDrone(final DroneDTO dto) {
        final Drone drone = new Drone();
        drone.setSerialNumber(dto.getSerialNumber());
        drone.setDroneModel(dto.getDroneModel());
        drone.setWeightLimit(dto.getWeightLimit());
        drone.setDroneState(dto.getDroneState());
        drone.setBatteryCapacity(dto.getBatteryCapacity());
        return drone;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public DroneModel getDroneModel() {
        return droneModel;
    }

    public int getWeightLimit() {
        return weightLimit;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public DroneState getDroneState() {
        return droneState;
    }
}
