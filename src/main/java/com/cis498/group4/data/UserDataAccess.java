package com.cis498.group4.data;

import com.cis498.group4.models.User;
import com.cis498.group4.util.DbConn;

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
     * Retrieves a single row from the users table in the database
     * @param id The ID of the row to retrieve
     * @return User object with the data from the row
     */
    public User getUser(int id) {
        // TODO: Match table and attribute names in DB
        User user = new User();

        try {
            // Set id parameter and execute SQL statement
            String sql = "SELECT * FROM app_user WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet results = preparedStatement.executeQuery();

            // Store results in User object
            if (results.next()) {
                user.setId(results.getInt("id"));
                user.setType(User.UserType.valueOf(results.getString("user_type").toUpperCase()));
                user.setFirstName(results.getString("first_name"));
                user.setLastName(results.getString("last_name"));
                user.setEmail(results.getString("email"));
                user.setPassword(results.getString("password"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * Retrieves all rows from users table in the database
     * @return List of User objects
     */
    public List<User> getAllUsers() {
        // TODO: Match table and attribute names in DB
        List<User> users = new ArrayList<User>();

        try {
            // Execute SQL statement - no parameters, so no need to prepare
            String sql = "SELECT * FROM app_user";
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            // Store results in list of Users
            while (results.next()) {
                User user = new User();
                user.setId(results.getInt("id"));
                user.setType(User.UserType.valueOf(results.getString("user_type").toUpperCase()));
                user.setFirstName(results.getString("first_name"));
                user.setLastName(results.getString("last_name"));
                user.setEmail(results.getString("email"));
                user.setPassword(results.getString("password"));

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
     * Inserts a new user into the database
     * @param user The User object to insert
     */
    public void insertUser(User user) {
        // TODO: Match table and attribute names in DB
        try {
            // Set parameters and execute SQL
            String sql = "INSERT INTO app_user(id, user_type, first_name, last_name, email, password) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getType().name());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setString(6, user.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the data of the user with the specified ID in the database
     * @param id The ID of the row to update
     * @param user The User object whose data is to be written over the row
     */
    public void updateUser(int id, User user) {
        // TODO: Match table and attribute names in DB
        try {
            // Set parameters and execute SQL
            String sql = "UPDATE app_user SET user_type=?, first_name=?, last_name=?, email=?, password=? WHERE userid=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getType().name());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setInt(6, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the user with the specified ID from the table in the database
     * @param id The ID of the row to delete
     */
    public void deleteUser(int id) {
        // TODO: Match table and attribute names in DB
        try {
            // Set id parameter and execute SQL
            String sql = "DELETE FROM app_user WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
