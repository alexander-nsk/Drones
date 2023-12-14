package com.musala.drones.facade;

import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.musala.drones.service.exception.drone.DroneDuplicateSerialNumberException;
import com.musala.drones.service.exception.drone.DroneIllegalStateException;
import com.musala.drones.service.exception.drone.DroneLowBatteryException;
import com.musala.drones.service.exception.drone.DroneOverWeightException;
import com.musala.drones.service.exception.medication.MedicationEmptyIncomeException;
import com.musala.drones.service.exception.medication.MedicationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<String> handleIllegalArgument(final IllegalArgumentException ex,
                                                              final WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidDefinitionException.class)
    public final ResponseEntity<String> handleInvalidDefinition(final InvalidDefinitionException ex,
                                                                final WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(DroneOverWeightException.class)
    public final ResponseEntity<String> handleConflict(final DroneOverWeightException ex,
                                                       final WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }

    @ExceptionHandler(DroneLowBatteryException.class)
    public final ResponseEntity<String> handleSecurity(final DroneLowBatteryException ex,
                                                       final WebRequest request) {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(ex.getMessage());
    }

    @ExceptionHandler(DroneIllegalStateException.class)
    public final ResponseEntity<Void> handleSecurity(final DroneIllegalStateException ex,
                                                     final WebRequest request) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(DroneDuplicateSerialNumberException.class)
    public final ResponseEntity<String> handleSecurity(final DroneDuplicateSerialNumberException ex,
                                                       final WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(MedicationEmptyIncomeException.class)
    public final ResponseEntity<String> handleSecurity(final MedicationEmptyIncomeException ex,
                                                       final WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The medications income are empty.");
    }

    @ExceptionHandler(MedicationNotFoundException.class)
    public final ResponseEntity<String> handleSecurity(final MedicationNotFoundException ex,
                                                       final WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't found the medications id the DB.");
    }
}