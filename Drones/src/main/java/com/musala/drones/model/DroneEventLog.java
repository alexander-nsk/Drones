package com.musala.drones.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DroneLog class represent drone logging information.
 */
@Entity
public class DroneEventLog {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime time;
    private String serialNumber;
    private int batteryCapacity;
    private DroneState droneState;

    public DroneEventLog() {
    }

    public static DroneEventLog fromDrone(final Drone drone) {
        final DroneEventLog eventLog = new DroneEventLog();
        eventLog.time = LocalDateTime.now();
        eventLog.serialNumber = drone.getSerialNumber();
        eventLog.batteryCapacity = drone.getBatteryCapacity();
        eventLog.droneState = drone.getDroneState();
        return eventLog;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public DroneState getDroneState() {
        return droneState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DroneEventLog that = (DroneEventLog) o;
        return batteryCapacity == that.batteryCapacity && time.equals(that.time) && serialNumber.equals(that.serialNumber) && droneState == that.droneState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, serialNumber, batteryCapacity, droneState);
    }

    @Override
    public String toString() {
        return "DroneEventLog{" +
                "time=" + time +
                ", serialNumber='" + serialNumber + '\'' +
                ", batteryCapacity=" + batteryCapacity +
                ", droneState=" + droneState +
                '}';
    }
}