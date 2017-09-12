package com.thotsoft.carpooling.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Apply {
    public static final String FIELD_ID = "id";
    public static final String FIELD_DATE = "date";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = FIELD_ID)
    private int id;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    private User user;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    private Advertisement advertisement;

    @Column(name = FIELD_DATE)
    @Temporal(TemporalType.DATE)
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Apply apply = (Apply) o;

        return id == apply.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Apply{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", advertisement=" + advertisement +
                ", date=" + date +
                '}';
    }
}
