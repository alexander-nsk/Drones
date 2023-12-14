package com.musala.drones.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class DroneMedication {
    private Long droneId;
    private Long medicationId;
    @Id
    @GeneratedValue
    private Long id;

    public DroneMedication() {
    }

    public DroneMedication(Long droneId, Long medicationId) {
        this.droneId = droneId;
        this.medicationId = medicationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DroneMedication that = (DroneMedication) o;
        return droneId.equals(that.droneId) && medicationId.equals(that.medicationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(droneId, medicationId);
    }

    public Long getMedicationId() {
        return medicationId;
    }

    public Long getDroneId() {
        return droneId;
    }
}