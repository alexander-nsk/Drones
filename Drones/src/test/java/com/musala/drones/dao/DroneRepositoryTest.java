package com.musala.drones.dao;

import com.musala.drones.model.Drone;
import com.musala.drones.service.DroneFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DroneRepositoryTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private DroneRepository droneRepository;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(droneRepository).isNotNull();
    }

    @Test
    void testFindBySerialNumber() {
        Drone drone = DroneFactory.s123_IDLE_100_500();
        droneRepository.save(drone);
        assertThat(droneRepository.findBySerialNumber(drone.getSerialNumber())).isNotNull();
    }

    @Test
    void testFindBySerialNumberDuplicate() {
        Drone droneIdle = DroneFactory.s123_IDLE_100_500();
        Drone droneLoading = DroneFactory.s1234_LOADING_100_500();
        droneRepository.save(droneIdle);
        droneRepository.save(droneLoading);

        List<Drone> availableDrones = droneRepository.findAvailableDrones();
        assertThat(availableDrones).isNotNull();
        assertThat(availableDrones.size() == 1).isTrue();
        assertThat(availableDrones.get(0).equals(droneIdle)).isTrue();
    }

    @Test
    void testGetBatteryCapacity() {
        Drone droneIdle = DroneFactory.s123_IDLE_100_500();
        droneRepository.save(droneIdle);

        Integer capacity = droneRepository.getBatteryCapacity(droneIdle.getId());
        assertThat(capacity).isNotNull();
        assertThat(capacity == droneIdle.getBatteryCapacity()).isTrue();
    }
}