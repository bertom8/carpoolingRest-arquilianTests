package com.thotsoft.carpooling.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class User implements Serializable {
    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_PHONE_NUMBER = "phoneNumber";
    public static final String FIELD_ADDRESS = "address";
    public static final String FIELD_ADMIN = "admin";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = FIELD_ID)
    private int id;

    @Column(name = FIELD_NAME, nullable = false)
    private String name;

    @Column(name = FIELD_EMAIL, nullable = false)
    private String email;

    @Column(name = FIELD_PASSWORD, nullable = false)
    private String password;

    @Column(name = FIELD_PHONE_NUMBER)
    private String phoneNumber;

    @Column(name = FIELD_ADDRESS)
    private Address address;

    @Column(name = FIELD_ADMIN, nullable = false)
    private boolean admin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        return id == user.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address=" + address +
                ", admin=" + admin +
                '}';
    }
}
