package com.musala.drones.facade;

import com.musala.drones.facade.dto.MedicationDTO;
import com.musala.drones.model.Medication;
import com.musala.drones.service.MedicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Tag(name = "Medications", description = "The Medications API.")
@Controller
@RequestMapping("/medication")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(final MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @Operation(summary = "Insert a new medication.", description = "Persist a new medication.")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Medication inserted")})
    @PostMapping(produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<Medication> add(@Valid @RequestBody final MedicationDTO medicationDto) {
        Medication insertedDrone = medicationService.insert(MedicationDTO.toMedication(medicationDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(insertedDrone);
    }
}