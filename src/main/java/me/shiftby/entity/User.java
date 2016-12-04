package me.shiftby.entity;

import javax.persistence.*;

@Entity
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
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;
    private Group[] groups;

    public User() {}
    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
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
    public Group[] getGroups() {
        return groups;
    }
    public void setGroups(Group[] groups) {
        this.groups = groups;
    }
}
