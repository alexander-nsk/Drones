package com.musala.drones.service;

import com.musala.drones.model.Medication;

public class MedicationFactory {
    public static Medication c123_Nazivin_50_i123() {
        return new Medication("nazivin", 50, "c123", "i123");
    }

    public static Medication c1234_Nurofen_100_i1234() {
        return new Medication("nurofen", 100, "c1234", "i1234");
    }
}
