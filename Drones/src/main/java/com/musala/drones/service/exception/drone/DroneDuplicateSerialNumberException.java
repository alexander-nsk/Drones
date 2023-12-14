package com.musala.drones.service.exception.drone;

public class DroneDuplicateSerialNumberException extends AbstractDroneRuntimeException {

    public DroneDuplicateSerialNumberException(String serialNumber) {
        super("There is an existing drone for the same serial number: " + serialNumber);
    }
}
