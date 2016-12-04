package me.shiftby.entity;

public enum Role {
    USER("USER"), ADMIN("ADMIN", USER);

    private Role[] roles;

    private String name;

    Role(String name, Role ... roles) {
        this.roles = roles;
    }

    public static boolean isGranted(Role role, Role userRole) {
        for (Role subRole : userRole.roles) {
            if (role == subRole || isGranted(role, subRole)) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public static Role fromString(String role) {
        return Role.valueOf(role);
    }

    @Override
    public String toString() {
        return name;
    }
}
