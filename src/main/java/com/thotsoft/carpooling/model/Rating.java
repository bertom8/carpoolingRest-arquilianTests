package com.thotsoft.carpooling.model;

import javax.persistence.*;

@Entity
public class Rating {
    public static final String FIELD_ID = "id";
    public static final String FIELD_RATING = "rating";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = FIELD_ID)
    private int id;

    @OneToOne(optional = false)
    private User giveRating;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    private User getRating;

    @Column(name = FIELD_RATING, nullable = false)
    private Rate rating;

    public enum Rate {
        BAD(1),
        NOT_BAD(2),
        AVERAGE(3),
        OK(4),
        EXCELLENT(5);

        int rate;

        Rate(int i) {
            rate = i;
        }

        public int getRate() {
            return rate;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getGiveRating() {
        return giveRating;
    }

    public void setGiveRating(User giveRating) {
        this.giveRating = giveRating;
    }

    public User getGetRating() {
        return getRating;
    }

    public void setGetRating(User getRating) {
        this.getRating = getRating;
    }

    public Rate getRating() {
        return rating;
    }

    public void setRating(Rate rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Rating rating = (Rating) o;

        return id == rating.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "id='" + id + '\'' +
                ", giveRating=" + giveRating +
                ", getRating=" + getRating +
                ", rating=" + rating +
                '}';
    }
}
