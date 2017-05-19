package com.cis498.group4.util;

import com.cis498.group4.models.User;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The UserHelpers class contains methods to assist with User data (verification, etc)
 */
public class UserHelpers {

    // Write status codes
    public static final int SUCCESSFUL_WRITE = 0;
    public static final int INVALID_DATA = 1;
    public static final int INVALID_NAME = 2;
    public static final int INVALID_EMAIL = 3;
    public static final int INVALID_PASSWORD = 4;
    public static final int REPEAT_PASSWORD = 5;
    public static final int INVALID_USER = 6;

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
     * Validates a user email address.
     * Not null, <= 256 chars, matches basic email address pattern
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
     * Validates a user password.
     * Password is not null or empty, and contains valid characters (https://www.owasp.org/index.php/Password_special_characters)
     * Use before writing to database.
     * @param password
     * @return
     */
    public static boolean validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile("[\\w !\\\"#$%&'\\(\\)\\*\\+,\\-\\.\\/\\\\:;<=>?@\\[\\]^_`\\{\\|\\}~]+");
        Matcher matcher = pattern.matcher(password);

        boolean length = password.length() <= 40;
        boolean characters = matcher.matches();

        return (length & characters);
    }

    /**
     * Validates that user type is either Guest or Organizer
     * @param type
     * @return
     */
    public static boolean validateType(User.UserType type) {
        if (type == User.UserType.GUEST || type == User.UserType.ORGANIZER) {
            return true;
        }

        return false;
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

    /**
     * Sets a user object's attributes based on parameters passed in request
     * @param user
     * @param request The HTTP request received from the client
     * @return writeStatus of created event
     */
    public static int setAttributesFromRequest(User user, HttpServletRequest request) {
        try {
            user.setType(User.UserType.valueOf(request.getParameter("type").trim()));
            user.setFirstName(request.getParameter("first-name").trim());
            user.setLastName(request.getParameter("last-name").trim());
            user.setEmail(request.getParameter("email").trim());

            // Get status message
            return writeStatus(user);

        } catch (Exception e) {
            return INVALID_DATA;
        }
    }

    /**
     * Get status code for verifying the success or failure of an insert or update operation
     * @param user
     * @return status code
     */
    public static int writeStatus(User user) {

        if (!validateName(user.getLastName())) {
            return INVALID_NAME;
        }

        if (!validateName(user.getFirstName())) {
            return INVALID_NAME;
        }

        if (!validateEmail(user.getEmail())) {
            return INVALID_EMAIL;
        }

        return SUCCESSFUL_WRITE;
    }

}
