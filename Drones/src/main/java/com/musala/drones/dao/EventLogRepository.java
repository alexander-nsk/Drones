package com.musala.drones.dao;

import com.musala.drones.model.DroneEventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventLogRepository extends JpaRepository<DroneEventLog, Long> {
}
