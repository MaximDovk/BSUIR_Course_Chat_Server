package me.shiftby.entity;

import me.shiftby.Main;
import me.shiftby.Session;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User creator;

    public Group() {
    }

    public Group(String name, User creator) {
        this.name = name;
        users = new ArrayList<>();
        this.creator = creator;
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
    public void addUser(User user) throws IOException {
        users.add(user);
        user.addGroup(this);
    }
    public void removeUser(User user) throws IOException {
        users.removeIf(u -> user.getId() == u.getId());
        user.removeGroup(this);
    }
    public User getCreator() {
        return creator;
    }
    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Set<String> getMembers() {
        return users.stream().map(User::getUsername).collect(Collectors.toSet());
    }
    public boolean isCreator(User user) {
        return user.getId() == creator.getId();
    }

    public void send(String message) {
        users.forEach(user -> {
            Session session = Main.getSessionManager().getByUsername(user.getUsername());
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
