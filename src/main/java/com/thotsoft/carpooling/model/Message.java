package com.thotsoft.carpooling.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Message implements Serializable {
    public static final String FIELD_ID = "id";
    public static final String FIELD_SOURCE = "source";
    public static final String FIELD_TARGET = "target";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_TEXT = "text";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_UNREAD = "unread";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = FIELD_ID)
    private int id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private User source;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private User target;

    @Column(name = FIELD_TITLE)
    private String title;

    @Column(name = FIELD_TEXT)
    private String text;

    @Column(name = FIELD_DATE, nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = FIELD_UNREAD, nullable = false)
    private boolean unread;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getSource() {
        return source;
    }

    public void setSource(User source) {
        this.source = source;
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Message message = (Message) o;

        return id == message.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", source=" + source +
                ", target=" + target +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", unread=" + unread +
                '}';
    }
}
