package com.musala.drones.dao;

import com.musala.drones.model.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {
    Drone findBySerialNumber(String serialNumber);

    @Query("select o from Drone o " +
            "where o.droneState = com.musala.drones.model.DroneState.IDLE")
    List<Drone> findAvailableDrones();

    @Query("select o.batteryCapacity from Drone o " +
            "where o.id = :droneId")
    Integer getBatteryCapacity(@Param("droneId") Long droneId);
}
