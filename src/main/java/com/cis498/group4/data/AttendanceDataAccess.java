package com.cis498.group4.data;

import com.cis498.group4.models.Attendance;
import com.cis498.group4.models.Event;
import com.cis498.group4.models.User;
import com.cis498.group4.util.DbConn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The AttendanceDataAccess class facilitates operations on event attendance data,
 * such as registration and sign-in status, in the database.
 */
public class AttendanceDataAccess {

    private Connection connection;

    private final String SELECT_ALL_ATTRIBUTES = "SELECT e.`event_id`, e.`event_name`, e.`start_date_time`, " +
            "e.`end_date_time`, p.`user_id` AS 'presenter_id', pt.`user_type` AS 'presenter_type', " +
            "p.`first_name` AS 'presenter_first_name', p.`last_name` AS 'presenter_last_name', " +
            "p.`email` AS 'presenter_email', e.`open_registration`, e.`registration_code`, " +
            "e.`mandatory_survey`, e.`capacity`, u.`user_id`, ut.`user_type`, u.`first_name`, " +
            "u.`last_name`, u.`email`, st.`attendance_status` " +
            "FROM `event_attendance` a " +
            "INNER JOIN `event` e ON a.`event_id` = e.`event_id` " +
            "INNER JOIN `user` p ON e.`presenter_id` = p.`user_id` " +
            "INNER JOIN `user_type` pt ON p.`user_type_id` = pt.`user_type_id` " +
            "INNER JOIN `user` u ON a.`user_id` = u.`user_id` " +
            "INNER JOIN `user_type` ut ON u.`user_type_id` = ut.`user_type_id` " +
            "INNER JOIN `attendance_status` st on a.`attendance_status_id` = st.`attendance_status_id`";

    public AttendanceDataAccess() {
        this.connection = DbConn.getConnection();
    }

    /**
     * Retrieves all attendance records
     * @return list of attendance records
     */
    public List<Attendance> getAttendanceRecords() {
        List<Attendance> attendanceRecords = new ArrayList<Attendance>();

        try {
            // Set parameters and execute SQL
            String sql = SELECT_ALL_ATTRIBUTES;
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            // Store results in list
            while (results.next()) {
                Attendance attendance = new Attendance();
                setAttributes(attendance, results);
                attendanceRecords.add(attendance);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return attendanceRecords;
    }

    /**
     * Retrieves the attendance list for an event
     * @param event The event whose attendance to retrieve
     * @return list of attendance records for the event
     */
    public List<Attendance> getEventAttendance(Event event) {
        List<Attendance> eventAttendance = new ArrayList<Attendance>();

        try {
            // Set parameters and execute SQL
            String sql = SELECT_ALL_ATTRIBUTES + " WHERE a.`event_id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, event.getId());
            ResultSet results = preparedStatement.executeQuery();

            // Store results in list
            while (results.next()) {
                Attendance attendance = new Attendance();
                setAttributes(attendance, results);
                eventAttendance.add(attendance);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return eventAttendance;
    }

    /**
     * Retrieves a list of events a user has attended
     * @param user The user whose attendance to retrieve
     * @return list of attendance records for the user
     */
    public List<Attendance> getUserAttendance(User user) {
        List<Attendance> userAttendance = new ArrayList<Attendance>();

        try {
            // Set parameters and execute SQL
            String sql = SELECT_ALL_ATTRIBUTES + " WHERE a.`user_id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getId());
            ResultSet results = preparedStatement.executeQuery();

            // Store results in list
            while (results.next()) {
                Attendance attendance = new Attendance();
                setAttributes(attendance, results);
                userAttendance.add(attendance);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return userAttendance;
    }

    /**
     * Creates a new event attendance entry in the database (effectively a new registration for a user for an event).
     * Uses default attendance status of NOT_ATTENDED
     * @param user The user to register
     * @param event The event for which to register
     * @return 0 for success, SQL error code for failure
     */
    public int register(User user, Event event) {
        try {
            // Set parameters and execute SQL
            String sql = "INSERT INTO `event_attendance`(`user_id`, `event_id`, `attendance_status_id`) " +
                         "VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(2, event.getId());
            preparedStatement.setInt(3, Attendance.AttendanceStatus.NOT_ATTENDED.ordinal());
            preparedStatement.executeUpdate();
            return 0;
        } catch (SQLException e) {
            return e.getErrorCode();
        }
    }

    /**
     * Removes the specified attendance entry from the database (effectively deregistering a user from an event)
     * @param userId The ID of the user to deregister
     * @param eventId The ID of the event from which to deregister
     * @return 0 for success, SQL error code for failure
     */
    public int deregister(int userId, int eventId) {
        try {
            // Set parameters and execute SQL
            String sql = "DELETE FROM `event_attendance` WHERE `user_id` = ? AND event_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, eventId);
            preparedStatement.executeUpdate();
            return 0;
        } catch (SQLException e) {
            return e.getErrorCode();
        }
    }

    /**
     * Updates an attendance status code for a given user and event
     * @param userId The ID of the user whose status is to be changed
     * @param eventId The ID of the event for which status change is being performed
     * @param status The ordinal status code to set
     * @return
     */
    public int updateStatus(int userId, int eventId, int status) {
        try {
            // Set parameters and execute SQL
            String sql = "UPDATE `event_attendance` SET `attendance_status_id` = ? " +
                         "WHERE `user_id` = ? AND event_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, status);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, eventId);
            return 0;
        } catch (SQLException e) {
            return e.getErrorCode();
        }
    }

    /**
     * Sets the attributes of an Attendance object based on the result set from a SQL query
     * @param attendance The Attendance object whose attributes to set
     * @param results The results set containing the data
     */
    private void setAttributes(Attendance attendance, ResultSet results) throws SQLException, IllegalArgumentException {
        User user = new User();
        user.setId(results.getInt("user_id"));
        user.setType(User.UserType.valueOf(results.getString("user_type").toUpperCase()));
        user.setFirstName(results.getString("first_name"));
        user.setLastName(results.getString("last_name"));
        user.setEmail(results.getString("email"));

        User presenter = new User();
        presenter.setId(results.getInt("presenter_id"));
        presenter.setType(User.UserType.valueOf(results.getString("presenter_type").toUpperCase()));
        presenter.setFirstName(results.getString("presenter_first_name"));
        presenter.setLastName(results.getString("presenter_last_name"));
        presenter.setEmail(results.getString("presenter_email"));

        Event event = new Event();
        event.setId(results.getInt("event_id"));
        event.setName(results.getString("event_name"));
        event.setStartDateTime(results.getTimestamp("start_date_time").toLocalDateTime());
        event.setEndDateTime(results.getTimestamp("end_date_time").toLocalDateTime());
        event.setPresenter(presenter);
        event.setOpenRegistration(results.getBoolean("open_registration"));
        event.setRegistrationCode(results.getString("registration_code"));
        event.setMandatorySurvey(results.getBoolean("mandatory_survey"));
        event.setCapacity(results.getInt("capacity"));

        attendance.setEvent(event);
        attendance.setUser(user);
        attendance.setStatus(
                Attendance.AttendanceStatus.valueOf(results.getString("attendance_status").toUpperCase()));

    }

}
