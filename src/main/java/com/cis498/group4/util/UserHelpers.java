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
     * Validates a user object's attributes before storing in DB
     * @param user The user to evaluate
     * @return true if the user is valid
     */
    public static boolean validateUser(User user) {
        boolean valid = false;

        return valid;
    }

}
