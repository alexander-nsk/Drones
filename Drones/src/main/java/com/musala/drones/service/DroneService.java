package com.musala.drones.service;

import com.musala.drones.dao.DroneRepository;
import com.musala.drones.dao.EventLogRepository;
import com.musala.drones.dao.LoadingRepository;
import com.musala.drones.facade.DispatchController;
import com.musala.drones.model.*;
import com.musala.drones.service.exception.drone.DroneDuplicateSerialNumberException;
import com.musala.drones.service.exception.drone.DroneIllegalStateException;
import com.musala.drones.service.exception.drone.DroneLowBatteryException;
import com.musala.drones.service.exception.drone.DroneNotFoundException;
import com.musala.drones.service.exception.medication.MedicationEmptyIncomeException;
import com.musala.drones.service.exception.medication.MedicationNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Service
@EnableAsync
public class DroneService {
    private static final Logger LOG = LoggerFactory.getLogger(DispatchController.class);

    private final DroneRepository droneRepository;
    private final LoadingRepository loadingRepository;
    private final MedicationService medicationService;
    private final EventLogRepository eventLogRepository;

    public DroneService(final DroneRepository droneRepository, LoadingRepository loadingRepository, MedicationService medicationService, EventLogRepository eventLogRepository) {
        this.droneRepository = droneRepository;
        this.loadingRepository = loadingRepository;
        this.medicationService = medicationService;
        this.eventLogRepository = eventLogRepository;
    }

    /**
     * Register the drone.
     * <p>
     * The methods checks that there is no other drones already present for the same serial number.
     *
     * @param drone the drone object.
     * @return the persisted drone.
     * @throws DroneDuplicateSerialNumberException if there is an existing drone for the same serial number.
     */
    public Drone register(final Drone drone) throws DroneDuplicateSerialNumberException {
        Drone droneInDb = droneRepository.findBySerialNumber(drone.getSerialNumber());
        if (droneInDb != null) {
            throw new DroneDuplicateSerialNumberException(droneInDb.getSerialNumber());
        }

        return droneRepository.save(drone);
    }

    /**
     * Load the drone with a given medications.
     * <p>
     * The methods checks that the drone state, battery capacity, medication list size and drones ability to get these
     * medication weight. And save the medications for the current drone.
     *
     * @param droneId       the drone id for loading.
     * @param medicationIds the collection of the medication ids
     * @return the loaded drone.
     * @throws DroneLowBatteryException         if the drones battery capacity is less than 25%
     * @throws MedicationNotFoundException if the medication list is empty
     */
    public Drone startLoading(final Long droneId, Set<Long> medicationIds)
            throws DroneLowBatteryException, MedicationNotFoundException {
        Drone drone = getDroneByIdWithState(droneId, DroneState.IDLE);

        if (isBatteryNotCharged(drone)) {
            throw new DroneLowBatteryException();
        }

        if (medicationIds.isEmpty()) {
            throw new MedicationEmptyIncomeException();
        }
        List<Medication> medicationList = medicationService.findAllById(medicationIds);
        if (medicationList.isEmpty()) {
            throw new MedicationNotFoundException();
        }

        List<DroneMedication> droneMedications = canLoadMedicationsList(drone, medicationList);
        loadingRepository.saveAll(droneMedications);

        drone.setDroneState(DroneState.LOADING);

        return droneRepository.save(drone);
    }

    /**
     * Change the drone state from LOADING to LOADED
     *
     * @param droneId the drone id for loading
     * @return
     */
    public Drone finishLoading(final Long droneId) {
        Drone drone = getDroneByIdWithState(droneId, DroneState.LOADING);
        drone.setDroneState(DroneState.LOADED);

        return droneRepository.save(drone);
    }

    public Drone startDelivering(final Long droneId) {
        Drone drone = getDroneByIdWithState(droneId, DroneState.LOADED);
        drone.setDroneState(DroneState.DELIVERING);

        return droneRepository.save(drone);
    }

    public Drone finishDelivering(final Long droneId, int capacity) {
        Drone drone = getDroneByIdWithState(droneId, DroneState.DELIVERING);
        drone.setDroneState(DroneState.DELIVERED);
        drone.setBatteryCapacity(capacity);

        loadingRepository.deleteAllByDroneId(drone.getId());

        return droneRepository.save(drone);
    }

    public Drone startReturning(final Long droneId) {
        Drone drone = getDroneByIdWithState(droneId, DroneState.DELIVERED);
        drone.setDroneState(DroneState.RETURNING);

        return droneRepository.save(drone);
    }

    public Drone finishReturning(final Long droneId, int capacity) {
        Drone drone = getDroneByIdWithState(droneId, DroneState.RETURNING);
        drone.setDroneState(DroneState.IDLE);
        drone.setBatteryCapacity(capacity);

        return droneRepository.save(drone);
    }

    public Drone update(final Long droneId, final Drone drone) {
        Drone droneToUpdate = getDroneById(droneId);

        droneToUpdate.setDroneState(drone.getDroneState());
        droneToUpdate.setBatteryCapacity(drone.getBatteryCapacity());
        droneToUpdate.setWeightLimit(drone.getWeightLimit());

        return droneRepository.save(droneToUpdate);
    }

    public List<Drone> getAvailableDrones() {
        return droneRepository.findAvailableDrones();
    }

    public List<DroneMedication> getDroneMedications(Long droneId) {
        return loadingRepository.findAllByDroneId(droneId);
    }

    public Integer getBatteryCapacity(Long droneId) {
        return droneRepository.getBatteryCapacity(droneId);
    }

    @Async
    @Scheduled(initialDelay = 30, fixedRate = 60, timeUnit = TimeUnit.SECONDS)
    public synchronized void logBatteriesInfo() {
        List<Drone> drones = droneRepository.findAll();
        if (drones.isEmpty()) {
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        List<DroneEventLog> droneEventLogs = new ArrayList<>();
        for (Drone drone : drones) {
            DroneEventLog droneEventLog = DroneEventLog.fromDrone(drone);
            stringBuilder.append(droneEventLog.toString());
        }

        eventLogRepository.saveAll(droneEventLogs);
        LOG.info(stringBuilder.toString());
    }

    public List<DroneEventLog> getDroneEventLog() {
        return eventLogRepository.findAll();
    }

    private Drone getDroneByIdWithState(Long droneId, DroneState droneState) {
        Drone drone = getDroneById(droneId);
        checkDroneState(drone, droneState);

        return drone;
    }

    private Drone getDroneById(Long droneId) {
        Optional<Drone> droneOptional = droneRepository.findById(droneId);
        if (droneOptional.isEmpty()) {
            throw new DroneNotFoundException(droneId);
        }

        return droneOptional.get();
    }

    private void checkDroneState(Drone drone, DroneState droneState) {
        if (!drone.getDroneState().equals(droneState)) {
            throw new DroneIllegalStateException(drone.getDroneState().name(), droneState.name());
        }
    }

    private boolean isBatteryNotCharged(final Drone drone) {
        return drone.getBatteryCapacity() < 25;
    }

    private List<DroneMedication> canLoadMedicationsList(Drone drone, List<Medication> medicationList) {
        List<DroneMedication> droneMedications = new ArrayList<>();
        if (canLoadMedications(drone, medicationList)) {
            for (Medication medication : medicationList) {
                droneMedications.add(new DroneMedication(drone.getId(), medication.getId()));
            }
        }

        return droneMedications;
    }

    private boolean canLoadMedications(final Drone drone, List<Medication> medications) {
        return getTotalWeight(medications) <= drone.getWeightLimit();
    }

    private int getTotalWeight(List<Medication> medications) {
        int totalWeight = 0;
        for (Medication medication : medications) {
            totalWeight += medication.getWeight();
        }

        return totalWeight;
    }
}