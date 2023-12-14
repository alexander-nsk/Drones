package com.musala.drones.service;

import com.musala.drones.model.Drone;
import com.musala.drones.model.DroneModel;
import com.musala.drones.model.DroneState;

public class DroneFactory {
    public static Drone s123_IDLE_100_500() {
        Drone drone = new Drone();
        drone.setSerialNumber("s123");
        drone.setDroneState(DroneState.IDLE);
        drone.setBatteryCapacity(100);
        drone.setWeightLimit(500);
        drone.setDroneModel(DroneModel.HEAVY_WEIGHT);

        return drone;
    }

    public static Drone s1234_LOADING_100_500() {
        Drone drone = new Drone();
        drone.setSerialNumber("s1234");
        drone.setDroneState(DroneState.LOADING);
        drone.setBatteryCapacity(100);
        drone.setWeightLimit(500);
        drone.setDroneModel(DroneModel.HEAVY_WEIGHT);

        return drone;
    }

    public static Drone s12345_IDLE_80_500() {
        Drone drone = new Drone();
        drone.setSerialNumber("s12345");
        drone.setDroneState(DroneState.IDLE);
        drone.setBatteryCapacity(80);
        drone.setWeightLimit(500);
        drone.setDroneModel(DroneModel.HEAVY_WEIGHT);

        return drone;
    }

}
