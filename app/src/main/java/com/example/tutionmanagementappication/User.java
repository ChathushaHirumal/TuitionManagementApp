// User.java  ───────────────
package com.example.tutionmanagementappication;

public class User {
    public final String username;
    public final String password;
    public final String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role     = role;
    }
}
