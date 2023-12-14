package com.musala.drones.facade;

import com.musala.drones.facade.dto.DroneDTO;
import com.musala.drones.model.Drone;
import com.musala.drones.model.DroneEventLog;
import com.musala.drones.model.DroneMedication;
import com.musala.drones.model.Medication;
import com.musala.drones.service.DroneService;
import com.musala.drones.service.MedicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Tag(name = "Drones", description = "The Drones API.")
@Controller
@RequestMapping("/drone")
public class DispatchController {
    private final DroneService droneService;
    private final MedicationService medicationService;

    public DispatchController(final DroneService droneService, MedicationService medicationService) {
        this.droneService = droneService;
        this.medicationService = medicationService;
    }

    @Operation(summary = "Register a new drone.", description = "Persist a new drone.")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Drone registered")})
    @PostMapping(produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<Drone> register(@Valid @RequestBody final DroneDTO dto) {
        Drone drone = DroneDTO.toDrone(dto);
        Drone insertedDrone = droneService.register(drone);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(insertedDrone);
    }

    @Operation(summary = "Load the drone.", description = "Load the drone by given medications.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Drone loaded")})
    @PostMapping(value = "/start_loading", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<Drone> startLoading(@Parameter(description = "Drone id", required = true) @Valid @RequestParam(value = "droneId") final Long droneId,
                                              @Parameter(description = "List of medications to load") @Valid @RequestBody final Set<Long> medications) {
        Drone insertedDrone = droneService.startLoading(droneId, medications);
        return ResponseEntity.status(HttpStatus.OK)
                .body(insertedDrone);
    }

    @Operation(summary = "Finish drone loading.", description = "Finish drone loading..")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Loading finished.")})
    @GetMapping(value = "/finish_loading", produces = {"application/json"})
    public ResponseEntity<Drone> finishLoading(@Parameter(description = "Id of the drone", required = true) @Valid @RequestParam(value = "id") final Long droneId) {
        Drone drone = droneService.finishLoading(droneId);
        return ResponseEntity.ok(drone);
    }

    @Operation(summary = "Start drone delivering.", description = "Start drone delivering.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Delivering started.")})
    @GetMapping(value = "/start_delivering", produces = {"application/json"})
    public ResponseEntity<Drone> startDelivering(@Parameter(description = "Id of the drone", required = true) @Valid @RequestParam(value = "id") final Long droneId) {
        Drone drone = droneService.startDelivering(droneId);
        return ResponseEntity.ok(drone);
    }

    @Operation(summary = "Finish drone delivering.", description = "Finish drone delivering.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Delivering finished.")})
    @GetMapping(value = "/finish_delivering", produces = {"application/json"})
    public ResponseEntity<Drone> finishDelivering(@Parameter(description = "Id of the drone", required = true) @Valid @RequestParam(value = "id") final Long droneId,
                                                  @Parameter(description = "Capacity of the drone", required = true) @Valid @RequestParam(value = "capacity") final int capacity) {
        Drone drone = droneService.finishDelivering(droneId, capacity);
        return ResponseEntity.ok(drone);
    }

    @Operation(summary = "Start drone returning.", description = "Start drone returning.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "returning started.")})
    @GetMapping(value = "/start_returning", produces = {"application/json"})
    public ResponseEntity<Drone> startReturning(@Parameter(description = "Id of the drone", required = true) @Valid @RequestParam(value = "id") final Long droneId) {
        Drone drone = droneService.startReturning(droneId);
        return ResponseEntity.ok(drone);
    }

    @Operation(summary = "Finish drone returning.", description = "Finish drone returning.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "returning finished.")})
    @GetMapping(value = "/finish_returning", produces = {"application/json"})
    public ResponseEntity<Drone> finishReturning(@Parameter(description = "Id of the drone", required = true) @Valid @RequestParam(value = "id") final Long droneId,
                                                 @Parameter(description = "Capacity of the drone", required = true) @Valid @RequestParam(value = "capacity") final int capacity) {
        Drone drone = droneService.finishReturning(droneId, capacity);

        return ResponseEntity.ok(drone);
    }

    @Operation(summary = "Check loaded medication items.", description = "Check loaded medication items for a given drone.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Loading checked.")})
    @GetMapping(value = "/check_loading", produces = {"application/json"})
    public ResponseEntity<List<Medication>> checkLoadedMedications(
            @Parameter(description = "Id of the drone", required = true) @Valid @RequestParam(value = "id") final Long droneId) {
        List<DroneMedication> droneMedications = droneService.getDroneMedications(droneId);
        if (droneMedications.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<Medication> medications = medicationService.findAllById(droneMedications);

        return ResponseEntity.ok(medications);
    }

    @Operation(summary = "Checking available drones for loading.", description = "Checking available drones for loading.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Checking successful.")})
    @GetMapping(value = "/available", produces = {"application/json"})
    public ResponseEntity<List<Drone>> getAvailableDrones() {
        List<Drone> availableDrones = droneService.getAvailableDrones();

        return ResponseEntity.ok(availableDrones);
    }

    @Operation(summary = "Сheck drone battery level for a given drone.", description = "Сheck drone battery level for a given drone.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Checking successful.")})
    @GetMapping(value = "/check_battery", produces = {"application/json"})
    public ResponseEntity<Integer> checkDroneBattery(
            @Parameter(description = "Id of the drone", required = true) @Valid @RequestParam(value = "id") final Long droneId) {
        Integer batteryCapacity = droneService.getBatteryCapacity(droneId);

        return ResponseEntity.ok(batteryCapacity);
    }

    @Operation(summary = "Update an existing drone.", description = "Update an already persisted drone.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Drone updated.")})
    @PutMapping(value = "/{droneId}", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<DroneDTO> update(
            @Parameter(description = "Id of the drone to update", required = true) @PathVariable("droneId") final Long droneId,
            @Parameter(description = "", required = true) @Valid @RequestBody final DroneDTO dto) {
        Drone drone = DroneDTO.toDrone(dto);
        Drone updateDrone = droneService.update(droneId, drone);
        return ResponseEntity.ok(DroneDTO.fromDrone(updateDrone));
    }


    @Operation(summary = "Write log batteries info.", description = "Write log batteries info for all drones using Log4j and Repository.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Log written.")})
    @GetMapping(value = "/write_log", produces = {"application/json"}, consumes = {"application/json"})
    public void logBatteriesInfo() {
        droneService.logBatteriesInfo();
    }

    @Operation(summary = "Get log batteries info.", description = "Get log batteries info for all drones using Log4j and Repository.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Log read.")})
    @GetMapping(value = "/read_log", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<List<DroneEventLog>> getLogBatteriesInfo() {
        List<DroneEventLog> droneEventLogs = droneService.getDroneEventLog();
        return ResponseEntity.ok(droneEventLogs);
    }
}