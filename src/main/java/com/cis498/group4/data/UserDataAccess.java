package com.cis498.group4.data;

import com.cis498.group4.models.User;
import com.cis498.group4.util.DbConn;
import com.cis498.group4.util.UserHelpers;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The UserDataAccess class facilitates operations on User data in the database.
 */
public class UserDataAccess {

    private Connection connection;

    public UserDataAccess() {
        this.connection = DbConn.getConnection();
    }

    /**
     * Retrieves a single row from the `user` table in the database
     * @param id The ID of the row to retrieve
     * @return User object with the data from the row
     */
    public User getUser(int id) {
        User user = new User();

        try {
            // Set id parameter and execute SQL statement
            // NOTE: passwords are stored in the DB as SHA-256 hashes
            // TODO: Eliminate joins for ENUMs, just get the ordinal number
            String sql = "SELECT u.`user_id`, ut.`user_type`, u.`first_name`, u.`last_name`, u.`email` " +
                         "FROM `user` u INNER JOIN `user_type` ut ON u.`user_type_id` = ut.`user_type_id` " +
                         "WHERE `user_id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet results = preparedStatement.executeQuery();

            // Store results in User object
            if (results.next()) {
                setAttributes(user, results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * Retrieves all rows from `user` table in the database
     * @return List of User objects
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();

        try {
            // Execute SQL statement - no parameters, so no need to prepare
            String sql = "SELECT u.`user_id`, ut.`user_type`, u.`first_name`, u.`last_name`, u.`email` " +
                         "FROM `user` u INNER JOIN `user_type` ut ON u.`user_type_id` = ut.`user_type_id`";
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            // Store results in list of Users
            while (results.next()) {
                User user = new User();
                setAttributes(user, results);
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Retrieves all users with type "ORGANIZER"
     * @return List of User objects
     */
    public List<User> getOrganizers() {
        List<User> organizers = new ArrayList<User>();

        try {
            // Execute SQL statement - no parameters, so no need to prepare
            String sql = "SELECT u.`user_id`, ut.`user_type`, u.`first_name`, u.`last_name`, u.`email` " +
                    "FROM `user` u INNER JOIN `user_type` ut ON u.`user_type_id` = ut.`user_type_id` WHERE u.`user_type_id` = 0";
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            // Store results in list of Users
            while (results.next()) {
                User user = new User();
                setAttributes(user, results);
                organizers.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return organizers;
    }

    /**
     * Gets the specified user's password hash from the database
     * @param user The user whose password to retrieve
     * @return Hex string value of the password hash or empty String for failure
     */
    public String getUserPasswordHash(User user) {
        String hash = "";

        try {
            // Set parameters and execute SQL
            String sql = "SELECT `password` FROM `user` WHERE `user_id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getId());
            ResultSet results = preparedStatement.executeQuery();

            if(results.next()) {
                hash = results.getString("password");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hash;
    }

    /**
     * Checks a user password by comparing its SHA hash to a stored hash
     * @param password The password to compare
     * @param user The user whose password is being checked
     * @return true if the hashes match, otherwise false
     */
    public boolean checkPassword(String password, User user) {
        try {
            String storedHash = getUserPasswordHash(user);
            String passHash = UserHelpers.shaHash(password);

            if (passHash.equals(storedHash)) {
                return true;
            } else {
                return false;
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Inserts a new user into the `user` table in the database
     * @param user The User object to insert
     * @return 0 for success, SQL error code for DB failure, int < 0 for encryption failure
     */
    public int insertUser(User user) {
        try {
            // Set parameters and execute SQL
            String sql = "INSERT INTO `user`(`user_type_id`, `first_name`, `last_name`, `email`, `password`) " +
                         "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getType().ordinal());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getEmail());
            // Only set password for new users
            preparedStatement.setString(5, UserHelpers.shaHash(user.getPassword()));
            preparedStatement.executeUpdate();
            return 0;
        } catch (SQLException e) {
            return e.getErrorCode();
        } catch (NoSuchAlgorithmException e) {
            return -1;
        } catch (UnsupportedEncodingException e) {
            return -2;
        }
    }

    /**
     * Updates the data of the user with the specified ID in the `user` table in the database
     * @param user The User object to update
     * @return 0 for success, SQL error code for failure
     */
    public int updateUser(User user) {
        try {
            // Set parameters and execute SQL
            String sql = "UPDATE `user` SET `user_type_id` = ?, `first_name` = ?, `last_name` = ?, `email` = ? " +
                         "WHERE `user_id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getType().ordinal());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getEmail());
            // Do not set password on update. Use updateUserPassword instead
            preparedStatement.setInt(5, user.getId());
            preparedStatement.executeUpdate();
            return 0;
        } catch (SQLException e) {
            return e.getErrorCode();
        }
    }

    /**
     * Updates user password by writing a new password hash to the DB
     * @param user The user whose password is to be updated
     * @param password The new password
     * @return 0 for success, SQL error code for DB failure, int < 0 for encryption failure
     */
    public int updateUserPassword(User user, String password) {
        try {
            String sql = "UPDATE `user` SET `password` = ? WHERE `user_id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, UserHelpers.shaHash(password));
            preparedStatement.setInt(2, user.getId());
            preparedStatement.executeUpdate();
            return 0;
        } catch (SQLException e) {
            return e.getErrorCode();
        } catch (NoSuchAlgorithmException e) {
            return -1;
        } catch (UnsupportedEncodingException e) {
            return -2;
        }
    }

    /**
     * Deletes the user with the specified ID from the `app_user` table in the database
     * @param id The ID of the user to delete
     * @return 0 for success, SQL error code for failure
     */
    public int deleteUser(int id) {
        try {
            // Set id parameter and execute SQL
            String sql = "DELETE FROM `user` WHERE `user_id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            return 0;
        } catch (SQLException e) {
            return e.getErrorCode();
        }
    }

    /**
     * Sets the attributes of a User object based on the result set from a SQL query
     * @param user The user whose attributes to set
     * @param results The results set containing the data
     */
    private void setAttributes(User user, ResultSet results) throws SQLException, IllegalArgumentException {
        user.setId(results.getInt("user_id"));
        user.setType(User.UserType.valueOf(results.getString("user_type").trim().toUpperCase()));
        user.setFirstName(results.getString("first_name"));
        user.setLastName(results.getString("last_name"));
        user.setEmail(results.getString("email"));
        // NOTE: Do not get passwords from DB with normal read operations, use getUserPassword
    }

}
