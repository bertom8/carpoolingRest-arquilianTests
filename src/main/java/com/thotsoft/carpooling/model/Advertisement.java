package com.thotsoft.carpooling.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Advertisement implements Serializable {
    public static final String FIELD_ID = "id";
    public static final String FIELD_SEEKING = "seeking";
    public static final String FIELD_FROMPLACE = "fromPlace";
    public static final String FIELD_TOPLACE = "toPlace";
    public static final String FIELD_START = "start";
    public static final String FIELD_COST = "cost";
    public static final String FIELD_UPLOADDATE = "uploadDate";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = FIELD_ID)
    private int id;

    @Column(name = FIELD_SEEKING, nullable = false)
    private boolean seeking;

    @Column(name = FIELD_FROMPLACE, nullable = false)
    private String fromPlace;

    @Column(name = FIELD_TOPLACE, nullable = false)
    private String toPlace;

    @Column(name = FIELD_START)
    @Temporal(TemporalType.DATE)
    private Date start;

    @Column(name = FIELD_COST)
    private int cost;

    @Column(name = FIELD_UPLOADDATE, nullable = false)
    @Temporal(TemporalType.DATE)
    private Date uploadDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSeeking() {
        return seeking;
    }

    public void setSeeking(boolean seeking) {
        this.seeking = seeking;
    }

    public String getFromPlace() {
        return fromPlace;
    }

    public void setFromPlace(String fromPlace) {
        this.fromPlace = fromPlace;
    }

    public String getToPlace() {
        return toPlace;
    }

    public void setToPlace(String toPlace) {
        this.toPlace = toPlace;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Advertisement that = (Advertisement) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Advertisement{" +
                "id=" + id +
                ", seeking=" + seeking +
                ", fromPlace='" + fromPlace + '\'' +
                ", toPlace='" + toPlace + '\'' +
                ", start=" + start +
                ", cost=" + cost +
                ", uploadDate=" + uploadDate +
                '}';
    }
}
