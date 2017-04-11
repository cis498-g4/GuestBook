package com.cis498.group4.models;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    // TODO: Default constructor for testing. Remove for production.
    public User(int id, UserType type, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.type = type;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
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

    /**
     * Returns a hex string SHA-256 message digest of the password
     */
    public String getPasswordEncrypted() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = null;
        md = MessageDigest.getInstance("SHA-256");
        md.reset();

        byte[] buffer = password.getBytes("UTF-8");
        md.update(buffer);
        byte[] digest = md.digest();

        String hex = "";

        for (int i = 0; i < digest.length; i++) {
            hex +=  Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
        }

        return hex;
    }

}
