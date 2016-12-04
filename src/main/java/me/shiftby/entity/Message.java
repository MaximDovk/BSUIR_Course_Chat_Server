package me.shiftby.entity;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id = -1;
    @ManyToOne(targetEntity = User.class)
    private User from;
    @ManyToOne(targetEntity = User.class)
    private User to;
    @Column(name = "message")
    private String message;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time")
    private Date time;


    public Message() {
    }
    public Message(User from, User to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
        time = Date.from(Instant.now());
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public User getFrom() {
        return from;
    }
    public void setFrom(User from) {
        this.from = from;
    }
    public User getTo() {
        return to;
    }
    public void setTo(User to) {
        this.to = to;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Date getTime() {
        return time;
    }
    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {

        return new StringBuilder()
                .append(from.getUsername())
                .append(" => ")
                .append(to.getUsername())
                .append(": ")
                .append(message)
                .toString();
    }
}
