package com.musala.drones.service.exception.drone;

public class DroneIllegalStateException extends AbstractDroneRuntimeException {

    public DroneIllegalStateException(String expState, String actualState) {
        super("Expected drone type is: " + expState + ", actual: " + actualState);
    }
}
