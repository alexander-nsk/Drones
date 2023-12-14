package com.musala.drones.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * Drone class represent drone entity.
 */
@Entity
public class Drone {

    @Id
    @GeneratedValue
    private Long id;
    /**
     * serial number (100 characters max);
     */
    @Column(unique = true)
    private String serialNumber;
    /**
     * model (Lightweight, Middleweight, Cruiserweight, Heavyweight);
     */
    @Enumerated(EnumType.ORDINAL)
    private DroneModel droneModel;
    /**
     * weight limit (500gr max);
     */
    private int weightLimit;
    /**
     * battery capacity (percentage);
     */
    private int batteryCapacity;
    /**
     * state (IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING).
     */
    @Enumerated(EnumType.ORDINAL)
    private DroneState droneState;

    public Drone() {
    }

    public Long getId() {
        return id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public DroneModel getDroneModel() {
        return droneModel;
    }

    public void setDroneModel(DroneModel droneModel) {
        this.droneModel = droneModel;
    }

    public int getWeightLimit() {
        return weightLimit;
    }

    public void setWeightLimit(int weightLimit) {
        this.weightLimit = weightLimit;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public DroneState getDroneState() {
        return droneState;
    }

    public void setDroneState(DroneState droneState) {
        this.droneState = droneState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Drone drone = (Drone) o;
        return weightLimit == drone.weightLimit && batteryCapacity == drone.batteryCapacity && id.equals(drone.id) && serialNumber.equals(drone.serialNumber) && droneModel == drone.droneModel && droneState == drone.droneState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serialNumber, droneModel, weightLimit, batteryCapacity, droneState);
    }

    @Override
    public String toString() {
        return "Drone{" +
                "id=" + id +
                ", serialNumber='" + serialNumber + '\'' +
                ", droneModel=" + droneModel +
                ", weightLimit=" + weightLimit +
                ", batteryCapacity=" + batteryCapacity +
                ", droneState=" + droneState +
                '}';
    }

    public String toBatteryLevelString() {
        return "Drone{" +
                ", serialNumber='" + serialNumber + '\'' +
                ", batteryCapacity=" + batteryCapacity +
                '}';
    }
}

