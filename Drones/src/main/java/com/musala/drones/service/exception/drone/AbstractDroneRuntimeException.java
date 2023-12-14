package com.musala.drones.service.exception.drone;

public abstract class AbstractDroneRuntimeException extends RuntimeException{
    public AbstractDroneRuntimeException(String message) {
        super(message);
    }
}
