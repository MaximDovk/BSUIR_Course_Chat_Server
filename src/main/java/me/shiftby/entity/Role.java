package me.shiftby.entity;

public enum Role {
    USER("USER"), ADMIN("ADMIN", USER);

    private Role[] roles;

    private String name;

    Role(String name, Role ... roles) {
        this.roles = roles;
        this.name = name;
    }

    public static boolean isGranted(Role role, Role userRole) {
        if (role == userRole) {
            return true;
        }
        for (Role subRole : userRole.roles) {
            if (role == subRole || isGranted(role, subRole)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return name;
    }
}
