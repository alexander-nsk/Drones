package com.musala.drones.dao;

import com.musala.drones.model.DroneMedication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoadingRepository extends JpaRepository<DroneMedication, Long> {
    @Query("select o from DroneMedication o " +
            "where o.droneId = :droneId")
    List<DroneMedication> findAllByDroneId(@Param("droneId") Long droneId);

    @Modifying
    @Query("delete from DroneMedication o " +
            "where o.droneId = :droneId")
    void deleteAllByDroneId(@Param("droneId") Long droneId);
}
