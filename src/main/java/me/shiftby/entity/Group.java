package me.shiftby.entity;

import me.shiftby.Session;
import me.shiftby.SessionManager;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "group")
@Table(name = "groups")
public class Group {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name", unique = true, length = 64, updatable = false)
    private String name;
    @ManyToMany(targetEntity = User.class, mappedBy = "groups", fetch = FetchType.LAZY)
    private List<User> users;

    public Group() {
    }
    public Group(String name) {
        this.name = name;
        users = new ArrayList<>();
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void addUser(User user) {
        users.add(user);
        user.addGroup(this);
    }
    public void removeUser(User user) {
        users.removeIf(u -> user.getId() == u.getId());
        user.removeGroup(this);
    }

    public void send(String message) {
        users.forEach(user -> {
            Session session = SessionManager.getInstance().getByUsername(user.getUsername());
            if (session != null) {
                try {
                    session.send(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
