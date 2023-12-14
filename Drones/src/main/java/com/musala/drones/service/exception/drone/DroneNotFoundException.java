package com.musala.drones.service.exception.drone;

public class DroneNotFoundException extends AbstractDroneRuntimeException {

    public DroneNotFoundException(Long droneId) {
        super("Drone not found for the given id: " + droneId);
    }
}
