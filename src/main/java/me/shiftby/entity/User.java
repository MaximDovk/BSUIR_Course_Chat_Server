package me.shiftby.entity;

import me.shiftby.orm.UserManager;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "user")
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "username", unique = true, length = 64)
    private String username;
    @Column(name = "password", length = 128)
    private String password;
    @Column(name = "role")
    private Role role;
    @ManyToMany(targetEntity = Group.class, fetch = FetchType.EAGER)
    private List<Group> groups;

    public User() {}
    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
        groups = new ArrayList<>();
    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    void addGroup(Group group) throws IOException {
        groups.add(group);
        UserManager.getInstance().changeUser(this);
    }
    void removeGroup(Group group) throws IOException {
        groups.removeIf(g -> group.getId() == g.getId());
        UserManager.getInstance().changeUser(this);
    }

    public Set<String> getGroups() {
        return groups.stream().map(Group::getName).collect(Collectors.toSet());
    }
}
