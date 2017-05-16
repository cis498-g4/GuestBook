package com.cis498.group4.data;

import com.cis498.group4.models.Event;
import com.cis498.group4.models.User;
import com.cis498.group4.util.DbConn;
import com.cis498.group4.util.EventHelpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

/**
 * The EventDataAccess class facilitates operations on Event data in the database.
 */
public class EventDataAccess {

    private Connection connection;

    // Default SQL selects all columns from the Event table, and the associated presenters in the User table
    private final String SELECT_ALL_ATTRIBUTES = "SELECT e.`event_id`, e.`event_name`, e.`start_date_time`, " +
            "e.`end_date_time`, u.`user_id`, ut.`user_type`, u.`first_name`, u.`last_name`, u.`email`, " +
            "e.`registration_code`, e.`open_registration`, e.`mandatory_survey`, e.`capacity` " +
            "FROM `event` e " +
            "INNER JOIN `user` u ON e.`presenter_id` = u.`user_id` " +
            "INNER JOIN `user_type` ut ON u.`user_type_id` = ut.`user_type_id`";

    // Get new database connection
    public EventDataAccess() {
        this.connection = DbConn.getConnection();
    }

    /**
     * Retrieves a single event from the database
     * @param id The ID of the row to retrieve
     * @return Event object with the data from the database
     */
    public Event getEvent(int id) {
        Event event = new Event();

        try {
            // Set id parameter and execute SQL statement
            String sql = SELECT_ALL_ATTRIBUTES + " WHERE e.`event_id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet results = preparedStatement.executeQuery();

            // Store results in Event object
            if (results.next()) {
                setAttributes(event, results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return event;
    }

    /**
     * Retrieves all events from the database
     * @return List of Event objects
     */
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<Event>();

        try {
            // Execute SQL statement - no parameters, so no need to prepare
            String sql = SELECT_ALL_ATTRIBUTES;
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            // Store results in List of Events
            while (results.next()) {
                Event event = new Event();
                setAttributes(event, results);
                events.add(event);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return events;
    }

    /**
     * Retrieves all events whose end times occur in the future
     * @return List of Event objects
     */
    public List<Event> getFutureEvents() {
        List<Event> events = new ArrayList<Event>();

        try {
            // Execute SQL
            String sql = SELECT_ALL_ATTRIBUTES + " WHERE e.`end_date_time` > NOW() ORDER BY e.`start_date_time`";
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            // Store results in List of Events
            while (results.next()) {
                Event event = new Event();
                setAttributes(event, results);
                events.add(event);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return events;
    }

    /**
     * Retrieves all events associated with a presenter whose end times occur in the future
     * @return List of Event objects
     */
    public List<Event> getPresenterFutureEvents(User presenter) {
        List<Event> events = new ArrayList<Event>();

        try {
            // Execute SQL
            String sql = SELECT_ALL_ATTRIBUTES +
                         " WHERE e.`presenter_id` = ? AND e.`end_date_time` > NOW() ORDER BY e.`start_date_time`";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, presenter.getId());
            ResultSet results = preparedStatement.executeQuery();

            // Store results in List of Events
            while (results.next()) {
                Event event = new Event();
                setAttributes(event, results);
                events.add(event);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return events;
    }

    /**
     * Retrieves an event by its unique registration code
     * @param registrationCode
     * @return Event matching the registration code
     */
    public Event getEventByRegistrationCode(String registrationCode) {
        Event event = new Event();

        try {
            // Set registration code parameter and execute SQL statement
            String sql = SELECT_ALL_ATTRIBUTES + " WHERE e.`registration_code` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, registrationCode);
            ResultSet results = preparedStatement.executeQuery();

            // Store results in Event object
            if (results.next()) {
                setAttributes(event, results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return event;
    }

    /**
     * Inserts a new user into the `event` table in the database
     * @param event The Event object to insert
     * @return 0 for success, -1 for invalid data, SQL error code for database failure
     */
    public int insertEvent(Event event) {
        if (!EventHelpers.validateFields(event)) {
            return -1;
        }

        try {
            // Set parameters and execute SQL
            String sql = "INSERT INTO `event`(`event_name`, `start_date_time`, `end_date_time`, `presenter_id`, " +
                         "`registration_code`, `open_registration`, `mandatory_survey`, `capacity`) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, event.getName().trim());
            preparedStatement.setString(2,
                    event.getStartDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS")));
            preparedStatement.setString(3,
                    event.getEndDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS")));
            preparedStatement.setInt(4, event.getPresenter().getId());
            preparedStatement.setString(5, event.getRegistrationCode());
            preparedStatement.setBoolean(6, event.isOpenRegistration());
            preparedStatement.setBoolean(7, event.isMandatorySurvey());
            preparedStatement.setInt(8, event.getCapacity());
            preparedStatement.executeUpdate();
            return 0;
        } catch (SQLException e) {
            return e.getErrorCode();
        }
    }

    /**
     * Updates the data of the event with the specified ID in the `event` table in the database
     * @param event The Event object whose data is to be written in the row
     * @return 0 for success, -1 for invalid data, SQL error code for database failure
     */
    public int updateEvent(Event event) {
        if (!EventHelpers.validateRecord(event)) {
            return -1;
        }

        try {
            // Set parameters and execute SQL
            String sql = "UPDATE `event` " +
                         "SET `event_name` = ?, `start_date_time` = ?, `end_date_time` = ?, `presenter_id` = ?, " +
                         "`registration_code` = ?, `open_registration` = ?, `mandatory_survey` = ?, " +
                         "`capacity` = ? " +
                         "WHERE `event_id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, event.getName().trim());
            preparedStatement.setString(2,
                    event.getStartDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS")));
            preparedStatement.setString(3,
                    event.getEndDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS")));
            preparedStatement.setInt(4, event.getPresenter().getId());
            preparedStatement.setString(5, event.getRegistrationCode());
            preparedStatement.setBoolean(6, event.isOpenRegistration());
            preparedStatement.setBoolean(7, event.isMandatorySurvey());
            preparedStatement.setInt(8, event.getCapacity());
            preparedStatement.setInt(9, event.getId());
            preparedStatement.executeUpdate();
            return 0;
        } catch (SQLException e) {
            return e.getErrorCode();
        }
    }

    /**
     * Deletes the event with the specified ID from the `event` table in the database
     * @param event The event to delete
     * @return 0 for success, SQL error code for DB failure
     */
    public int deleteEvent(Event event) {
        try {
            // Set id parameter and execute SQL
            String sql = "DELETE FROM `event` WHERE `event_id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, event.getId());
            preparedStatement.executeUpdate();
            return 0;
        } catch (SQLException e) {
            return e.getErrorCode();
        }
    }

    /**
     * Sets the attributes of a Event object based on the result set from a SQL query
     * @param event The Event whose attributes to set
     * @param results The results set containing the data
     */
    private void setAttributes(Event event, ResultSet results) throws SQLException, IllegalArgumentException {
        User user = new User();
        user.setId(results.getInt("user_id"));
        user.setType(User.UserType.valueOf(escapeHtml4(results.getString("user_type").toUpperCase())));
        user.setFirstName(escapeHtml4(results.getString("first_name")));
        user.setLastName(escapeHtml4(results.getString("last_name")));
        user.setEmail(escapeHtml4(results.getString("email")));
        // NOTE: Do not retrieve password from DB, only use the data to check

        event.setId(results.getInt("event_id"));
        event.setName(escapeHtml4(results.getString("event_name")));
        event.setStartDateTime(results.getTimestamp("start_date_time").toLocalDateTime());
        event.setEndDateTime(results.getTimestamp("end_date_time").toLocalDateTime());
        event.setPresenter(user);
        event.setRegistrationCode(escapeHtml4(results.getString("registration_code")));
        event.setOpenRegistration(results.getBoolean("open_registration"));
        event.setMandatorySurvey(results.getBoolean("mandatory_survey"));
        event.setCapacity(results.getInt("capacity"));
    }

}
