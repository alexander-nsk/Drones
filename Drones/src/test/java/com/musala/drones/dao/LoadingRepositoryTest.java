package com.musala.drones.dao;

import com.musala.drones.model.DroneMedication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LoadingRepositoryTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private LoadingRepository loadingRepository;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(loadingRepository).isNotNull();
    }

    @Test
    void testFindAllByDroneId() {
        loadingRepository.saveAll(getTestDroneMedicationList());

        List<DroneMedication> droneMedicationListById = loadingRepository.findAllByDroneId(1L);
        assertThat(droneMedicationListById).isNotNull();
        assertThat(droneMedicationListById.size() == 2).isTrue();
        assertThat(droneMedicationListById.get(0).getDroneId() == 1L).isTrue();
        assertThat(droneMedicationListById.get(1).getDroneId() == 1L).isTrue();
    }

    @Test
    void testDeleteAllByDroneId() {
        loadingRepository.saveAll(getTestDroneMedicationList());

        loadingRepository.deleteAllByDroneId(1L);
        List<DroneMedication> droneMedicationListById = loadingRepository.findAllByDroneId(1L);
        assertThat(droneMedicationListById).isNotNull();
        assertThat(droneMedicationListById.isEmpty()).isTrue();
    }

    private List<DroneMedication> getTestDroneMedicationList() {
        List<DroneMedication> droneMedicationList = new ArrayList<>();
        droneMedicationList.add(new DroneMedication(1L, 3L));
        droneMedicationList.add(new DroneMedication(1L, 2L));
        droneMedicationList.add(new DroneMedication(2L, 1L));

        return droneMedicationList;
    }
}