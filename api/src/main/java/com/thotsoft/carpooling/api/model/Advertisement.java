package com.thotsoft.carpooling.api.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "seeking", nullable = false)
    private boolean seeking;

    @Column(name = "fromPlace", nullable = false)
    private String fromPlace;

    @Column(name = "toPlace", nullable = false)
    private String toPlace;

    @Column(name = "start")
    @Temporal(TemporalType.DATE)
    private Date start;

    @Column(name = "cost")
    private int cost;

    @Column(name = "uploadDate", nullable = false)
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
