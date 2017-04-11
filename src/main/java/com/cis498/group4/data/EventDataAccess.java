package com.cis498.group4.data;

import com.cis498.group4.models.Event;
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
 * The EventDataAccess class facilitates operations on Event data in the database.
 */
public class EventDataAccess {

    private Connection connection;

    public EventDataAccess(Connection connection) {
        this.connection = DbConn.getConnection();
    }

    public Event getEvent(int id) {
        // TODO: Match table and attribute names in DB
        Event event = new Event();

        try {
            // Set id parameter and execute SQL statement
            String sql = "SELECT e.id AS event_id, e.title, e.start_date_time, e.end_date_time, e.registration_code, " +
                    "e.open_registration, e.capacity, u.id AS user_id FROM event e INNER JOIN app_user u ON e.presenter_id = a.id WHERE e.id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet results = preparedStatement.executeQuery();

            // Store results in Event object
            if (results.next()) {
                User user = new User();
                user.setId(results.getInt("user_id"));
                user.setType(User.UserType.valueOf(results.getString("user_type").toUpperCase()));
                user.setFirstName(results.getString("first_name"));
                user.setLastName(results.getString("last_name"));
                user.setEmail(results.getString("email"));
                user.setPassword(results.getString("password"));    // TODO: should we get password?

                event.setId(results.getInt("event_id"));
                event.setName(results.getString("title"));
                event.setStartDateTime(results.getTimestamp("start_date_time").toLocalDateTime());
                event.setEndDateTime(results.getTimestamp("end_date_time").toLocalDateTime());
                event.setPresenter(user);
                event.setRegistrationCode(results.getString("registration_code"));
                event.setOpenRegistration(results.getBoolean("open_registration"));
                event.setCapacity(results.getInt("capacity"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return event;
    }

    public List<Event> getAllEvents() {
        // TODO: Match table and attribute names in DB
        List<Event> events = new ArrayList<Event>();

        try {
            // Execute SQL statement - no parameters, so no need to prepare
            String sql = "SELECT * FROM event";
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            // Store results in List of Events
            while (results.next()) {
                User user = new User();
                user.setId(results.getInt("user_id"));
                user.setType(User.UserType.valueOf(results.getString("user_type").toUpperCase()));
                user.setFirstName(results.getString("first_name"));
                user.setLastName(results.getString("last_name"));
                user.setEmail(results.getString("email"));
                user.setPassword(results.getString("password"));    // TODO: should we get password?

                Event event = new Event();
                event.setId(results.getInt("id"));
                event.setName(results.getString("title"));
                event.setStartDateTime(results.getTimestamp("start_date_time").toLocalDateTime());
                event.setEndDateTime(results.getTimestamp("end_date_time").toLocalDateTime());
                event.setPresenter(user);
                event.setRegistrationCode(results.getString("registration_code"));
                event.setOpenRegistration(results.getBoolean("open_registration"));
                event.setCapacity(results.getInt("capacity"));

                events.add(event);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return events;
    }

    public void insertEvent(Event event) {
        // TODO: Match table and attribute names in DB
        try {
            // TODO: Set parameters and execute SQL
            String sql = "INSERT INTO event(id, title, start_date_time, end_date_time, presenter_id, registration_code, open_registration, capacity) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //TODO

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEvent(int id, Event event) {
        // TODO: Match table and attribute names in DB
        try {
            // TODO: Set parameters and execute SQL
            String sql = "";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEvent(int id) {
        // TODO: Match table and attribute names in DB
        try {
            // TODO: Set id parameter and execute SQL
            String sql = "";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
