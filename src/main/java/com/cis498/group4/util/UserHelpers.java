package com.cis498.group4.util;

import com.cis498.group4.models.User;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The UserHelpers class contains methods to assist with User data (verification, etc)
 */
public class UserHelpers {

    /**
     * Validates a user record (e.g. required fields are not null).
     * Use before writing to database.
     * @param user
     * @return
     */
    public static boolean validate(User user) {
        //TODO
        return true;
    }

    /**
     * Validates a user password (e.g. length).
     * Use before writing to database.
     * @param password
     * @return
     */
    public static boolean validatePassword(String password) {
        //TODO
        return true;
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
