package com.cis498.group4.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The UserHelpers class contains methods to assist with User data (verification, etc)
 */
public class UserHelpers {

    /**
     * Returns a hex string SHA-256 message digest of the password
     */
    public static String sha256(String message) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = null;
        md = MessageDigest.getInstance("SHA-256");
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
