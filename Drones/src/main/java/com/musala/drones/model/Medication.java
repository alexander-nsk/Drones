package com.musala.drones.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Medication class represent drone medication.
 */
@Entity
public class Medication {
    private String name;
    private int weight;
    private String code;
    private String image;
    @Id
    @GeneratedValue
    private Long id;

    public Medication() {
    }

    public Medication(String name, int weight, String code, String image) {
        this.name = name;
        this.weight = weight;
        this.code = code;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medication that = (Medication) o;
        return weight == that.weight && name.equals(that.name) && code.equals(that.code) && image.equals(that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, weight, code, image);
    }

    @Override
    public String toString() {
        return "Medication{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", code='" + code + '\'' +
                ", image='" + image + '\'' +
                ", id=" + id +
                '}';
    }
}
