package com.thotsoft.carpooling.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

public class Address implements Serializable {
    public static final String FIELD_ZIPCODE = "zipCode";
    public static final String FIELD_CITY = "city";
    public static final String FIELD_STREET = "street";
    public static final String FIELD_HOUSENUMBER = "houseNumber";

    @Column(name = FIELD_ZIPCODE)
    private int zipCode;
    @Column(name = FIELD_CITY)
    private String city;
    @Column(name = FIELD_STREET)
    private String street;
    @Column(name = FIELD_HOUSENUMBER)
    private String houseNumber;

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Address address = (Address) o;

        if (zipCode != address.zipCode) {
            return false;
        }
        if (city != null ? !city.equals(address.city) : address.city != null) {
            return false;
        }
        if (street != null ? !street.equals(address.street) : address.street != null) {
            return false;
        }
        return houseNumber != null ? houseNumber.equals(address.houseNumber) : address.houseNumber == null;
    }

    @Override
    public int hashCode() {
        int result = zipCode;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (houseNumber != null ? houseNumber.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Address{" +
                "zipCode=" + zipCode +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                '}';
    }
}
