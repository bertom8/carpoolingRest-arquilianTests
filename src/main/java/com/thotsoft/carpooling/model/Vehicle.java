package com.thotsoft.carpooling.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Vehicle implements Serializable {
    public static final String FIELD_LICENCE_NUMBER = "licenceNumber";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_MANUFACTURER = "manufacturer";
    public static final String FIELD_DESING = "design";
    public static final String FIELD_COLOR = "color";
    public static final String FIELD_SMOKING = "smoking";
    public static final String FIELD_CLIME = "clime";
    public static final String FIELD_ANIMAL = "animal";
    public static final String FIELD_ROAD = "road";
    public static final String FIELD_COMFORT = "comfort";

    @Id
    @Column(name = FIELD_LICENCE_NUMBER, nullable = false)
    private String licenceNumber;

    @Column(name = FIELD_TYPE)
    private String type;

    @Column(name = FIELD_MANUFACTURER)
    private String manufacturer;

    @Column(name = FIELD_DESING)
    private String design;

    @Column(name = FIELD_COLOR)
    private String color;

    @Column(name = FIELD_SMOKING, nullable = false)
    private boolean smoking;

    @Column(name = FIELD_CLIME, nullable = false)
    private boolean clime;

    @Column(name = FIELD_ANIMAL, nullable = false)
    private boolean animal;

    @Column(name = FIELD_ROAD, nullable = false)
    private boolean road;

    @Column(name = FIELD_COMFORT, nullable = false)
    private boolean comfort;

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDesign() {
        return design;
    }

    public void setDesign(String design) {
        this.design = design;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isSmoking() {
        return smoking;
    }

    public void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }

    public boolean isClime() {
        return clime;
    }

    public void setClime(boolean clime) {
        this.clime = clime;
    }

    public boolean isAnimal() {
        return animal;
    }

    public void setAnimal(boolean animal) {
        this.animal = animal;
    }

    public boolean isRoad() {
        return road;
    }

    public void setRoad(boolean road) {
        this.road = road;
    }

    public boolean isComfort() {
        return comfort;
    }

    public void setComfort(boolean comfort) {
        this.comfort = comfort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Vehicle vehicle = (Vehicle) o;

        return licenceNumber != null ? licenceNumber.equals(vehicle.licenceNumber) : vehicle.licenceNumber == null;
    }

    @Override
    public int hashCode() {
        return licenceNumber != null ? licenceNumber.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "licenceNumber='" + licenceNumber + '\'' +
                ", type='" + type + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", design='" + design + '\'' +
                ", color='" + color + '\'' +
                ", smoking=" + smoking +
                ", clime=" + clime +
                ", animal=" + animal +
                ", road=" + road +
                ", comfort=" + comfort +
                '}';
    }
}
