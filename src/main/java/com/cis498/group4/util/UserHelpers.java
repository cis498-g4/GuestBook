package com.cis498.group4.util;

import com.cis498.group4.models.User;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The UserHelpers class contains methods to assist with User data (verification, etc)
 */
public class UserHelpers {

    /**
     * Validates an existing user record.
     * User is not null, has valid id, type, firstName, lastName, email
     * Use before writing to database.
     * @param user
     * @return
     */
    public static boolean validateRecord(User user) {
        if (user == null) {
            return false;
        }

        if (user.getId() < 1) {
            return false;
        }

        return (validateFields(user));
    }

    /**
     * Validates basic user fields.
     * User is not null, has valid type, firstName, lastName, email
     * Use before writing new user to database.
     * @param user
     * @return
     */
    public static boolean validateFields(User user) {
        if (user == null) {
            return false;
        }

        boolean type = validateType(user.getType());
        boolean lastName = validateName(user.getLastName());
        boolean firstName = validateName(user.getFirstName());
        boolean email = validateEmail(user.getEmail());

        return (type && lastName && firstName && email);
    }


    /**
     * Validates a user email address using VERY basic criteria
     * (e.g. one or more characters followed by an @ and one or more characters)
     * @param email
     * @return The logical AND of several boolean checks (e.g. length)
     */
    public static boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile("^[\\w\\-]+(?:\\.[\\w\\-]+)*@[\\w\\-]+(?:\\.[\\w\\-]+)*$");
        Matcher matcher = pattern.matcher(email);

        boolean length = (email.trim().length() <= 256);
        boolean format = matcher.matches();

        return (length && format);
    }

    /**
     * Validates a user first name / last name
     * @param name
     * @return Logical AND of several boolean expressions (e.g. length <= 64)
     */
    public static boolean validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile("^[^<>=:;\\/\\\\]+$");
        Matcher matcher = pattern.matcher(name);

        boolean length = (name.trim().length() <= 64);
        boolean characters = matcher.matches();

        return (characters && length);
    }

    /**
     * Validates a user password (e.g. length).
     * Use before writing to database.
     * @param password
     * @return
     */
    public static boolean validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }

        return true;
    }

    public static boolean validateType(User.UserType type) {
        if (type == User.UserType.GUEST || type == User.UserType.ORGANIZER) {
            return true;
        }

        return false;
    }

    /**
     * Validates a user record read from a CSV (lastName, firstName, email)
     * @param user
     * @return
     */
    public static boolean validateCSVUser(User user) {
        if (user == null) {
            return false;
        }

        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            return false;
        }

        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            return false;
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return false;
        }

        boolean lastName = validateName(user.getLastName());
        boolean firstName = validateName(user.getFirstName());
        boolean email = validateEmail(user.getEmail());

        return (lastName && firstName && email);
    }

    /**
     * Returns a hex string SHA-1 message digest of the password
     */
    public static String shaHash(String message) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = null;
        md = MessageDigest.getInstance("SHA-1");
        md.reset();

        byte[] buffer = message.getBytes("UTF-8");
        md.update(buffer);
        byte[] digest = md.digest();

        String hex = "";

        for (int i = 0; i < digest.length; i++) {
            hex +=  Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
        }

        return hex;
    }

}
