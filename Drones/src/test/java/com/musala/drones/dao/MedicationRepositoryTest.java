package com.musala.drones.dao;

import com.musala.drones.model.Medication;
import com.musala.drones.service.MedicationFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MedicationRepositoryTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private MedicationRepository medicationRepository;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(medicationRepository).isNotNull();
    }

    @Test
    void testSave() {
        Medication medication = MedicationFactory.c123_Nazivin_50_i123();
        assertThat(medicationRepository.save(medication)).isNotNull();
    }
}