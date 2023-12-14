package com.musala.drones.facade.dto;

import com.musala.drones.model.Medication;

/**
 * Data transfer object for {@link Medication}.
 */
public class MedicationDTO {
    private String name;
    private int weight;
    private String code;
    private String image;

    public static Medication toMedication(final MedicationDTO medicationDto) {
        Medication medication = new Medication();
        medication.setCode(medicationDto.getCode());
        medication.setName(medicationDto.getName());
        medication.setWeight(medicationDto.getWeight());
        medication.setImage(medicationDto.getImage());

        return medication;
    }

    public static MedicationDTO fromMedication(final Medication medication) {
        final MedicationDTO dto = new MedicationDTO();
        dto.name = medication.getName();
        dto.weight = medication.getWeight();
        dto.code = medication.getCode();
        dto.image = medication.getImage();
        return dto;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public String getCode() {
        return code;
    }

    public String getImage() {
        return image;
    }
}
