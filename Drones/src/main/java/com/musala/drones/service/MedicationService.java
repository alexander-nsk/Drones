package com.musala.drones.service;

import com.musala.drones.dao.MedicationRepository;
import com.musala.drones.model.DroneMedication;
import com.musala.drones.model.Medication;
import com.musala.drones.service.exception.medication.MedicationNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MedicationService {
    private final MedicationRepository medicationRepository;

    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    /**
     * Insert the medication.
     * <p>
     *
     * @param medication the medication object.
     * @return the persisted medication.
     */
    public Medication insert(final Medication medication) {
        return medicationRepository.save(medication);
    }

    public List<Medication> findAllById(Set<Long> medications) {
        List<Medication> medicationList = medicationRepository.findAllById(medications);
        if (medicationList.isEmpty()) {
            throw new MedicationNotFoundException();
        }

        return medicationList;
    }

    public List<Medication> findAllById(List<DroneMedication> medications) {
        Set<Long> meditationIds = new HashSet<>();
        for (DroneMedication droneMedication : medications) {
            meditationIds.add(droneMedication.getMedicationId());
        }

        return findAllById(meditationIds);
    }
}