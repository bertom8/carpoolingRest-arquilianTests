package com.thotsoft.carpooling.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Vehicle {

    @Id
    @Column(name = "licenceNumber", nullable = false)
    private String licenceNumber;

    @Column(name = "type")
    private String type;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "design")
    private String design;

    @Column(name = "color")
    private String color;

    @Column(name = "smoking", nullable = false)
    private boolean smoking;

    @Column(name = "clime", nullable = false)
    private boolean clime;

    @Column(name = "animal", nullable = false)
    private boolean animal;

    @Column(name = "road", nullable = false)
    private boolean road;

    @Column(name = "comfort", nullable = false)
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
