package com.cis498.group4.models;

import java.io.Serializable;

/**
 * The User class is a JavaBean data object representing a user
 */
public class User implements Serializable {
    public static final long serialVersionUID = 1L;

    public enum UserType {ORGANIZER, GUEST}

    private int id;
    private UserType type;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public User() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
