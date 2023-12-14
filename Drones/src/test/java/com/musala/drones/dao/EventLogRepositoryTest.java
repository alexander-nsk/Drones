package com.musala.drones.dao;

import com.musala.drones.model.Drone;
import com.musala.drones.model.DroneEventLog;
import com.musala.drones.service.DroneFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EventLogRepositoryTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private EventLogRepository eventLogRepository;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(eventLogRepository).isNotNull();
    }

    @Test
    void testSave() {
        Drone drone = DroneFactory.s123_IDLE_100_500();
        DroneEventLog droneEventLog = DroneEventLog.fromDrone(drone);
        assertThat(eventLogRepository.save(droneEventLog)).isNotNull();
    }
}