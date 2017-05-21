package com.cis498.group4.data;

import com.cis498.group4.models.Attendance;
import com.cis498.group4.models.Event;
import com.cis498.group4.models.User;
import com.cis498.group4.util.AttendanceHelpers;
import com.cis498.group4.util.DbConn;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

/**
 * The AttendanceDataAccess class facilitates operations on event attendance data,
 * such as registration and sign-in status, in the database.
 */
public class AttendanceDataAccess {

    private Connection connection;

    // Default SQL selects all columns from Attendance table and linked User and Event tables
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

    // Get new database connection
    public AttendanceDataAccess() {
        this.connection = DbConn.getConnection();
    }

    /**
     * Retrieves a single attendance record, by user and event
     * @param userId The user ID of the record to find
     * @param eventId The event ID of the record to find
     * @return Attendance record
     */
    public Attendance getAttendance(int userId, int eventId) {
        Attendance attendance = new Attendance();

        try {
            // Get attendance count
            Map<Integer, Integer> attendanceCount = new HashMap<Integer, Integer>();
            attendanceCount.put(eventId, getAttendanceCount(eventId));

            String sql = SELECT_ALL_ATTRIBUTES + " WHERE a.`user_id` = ? AND a.`event_id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, eventId);
            ResultSet results = preparedStatement.executeQuery();

            if (results.next()) {
                setAttributes(attendance, attendanceCount, results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attendance;
    }

    /**
     * Retrieves all attendance records
     * @return list of attendance records
     */
    public List<Attendance> getAttendanceRecords() {
        List<Attendance> attendanceRecords = new ArrayList<Attendance>();

        try {
            // Get attendance counts
            Map<Integer, Integer> attendanceCounts = getAttendanceCounts();

            // Set parameters and execute SQL
            String sql = SELECT_ALL_ATTRIBUTES;
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            // Store results in list
            while (results.next()) {
                Attendance attendance = new Attendance();
                setAttributes(attendance, attendanceCounts, results);
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
            // Get attendance counts
            Map<Integer, Integer> attendanceCounts = getAttendanceCounts();

            // Set parameters and execute SQL
            String sql = SELECT_ALL_ATTRIBUTES + " WHERE a.`event_id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, event.getId());
            ResultSet results = preparedStatement.executeQuery();

            // Store results in list
            while (results.next()) {
                Attendance attendance = new Attendance();
                setAttributes(attendance, attendanceCounts, results);
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
     * Retrieves a list of events a user is associated with
     * @param user The user whose attendance to retrieve
     * @return list of attendance records for the user
     */
    public List<Attendance> getUserAttendance(User user) {
        List<Attendance> userAttendance = new ArrayList<Attendance>();

        try {
            // Get attendance counts
            Map<Integer, Integer> attendanceCounts = getAttendanceCounts();

            // Set parameters and execute SQL
            String sql = SELECT_ALL_ATTRIBUTES + " WHERE a.`user_id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getId());
            ResultSet results = preparedStatement.executeQuery();

            // Store results in list
            while (results.next()) {
                Attendance attendance = new Attendance();
                setAttributes(attendance, attendanceCounts, results);
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
     * Retrieves a list of events a user is registered for in the future
     * @param user The user whose attendance to retrieve
     * @return list of attendance records for the user
     */
    public List<Attendance> getFutureRegistrations(User user) {
        List<Attendance> futureRegistration = new ArrayList<Attendance>();

        try {
            // Get attendance counts
            Map<Integer, Integer> attendanceCounts = getAttendanceCounts();

            // Set parameters and execute SQL
            String sql = SELECT_ALL_ATTRIBUTES +
                         " WHERE a.`user_id` = ? AND e.`end_date_time` > NOW() AND a.`attendance_status_id` = 0";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getId());
            ResultSet results = preparedStatement.executeQuery();

            // Store results in list
            while (results.next()) {
                Attendance attendance = new Attendance();
                setAttributes(attendance, attendanceCounts, results);
                futureRegistration.add(attendance);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return futureRegistration;
    }

    /**
     * Gets a list of attendances where the user has not completed a survey
     * @param user The user whose attendances to retrieve
     * @return List of attendance records for the user where there is no associated survey
     */
    public List<Attendance> getPendingSurveys(User user) {
        List<Attendance> pendingSurveys = new ArrayList<Attendance>();

        try {
            // Get attendance counts
            Map<Integer, Integer> attendanceCounts = getAttendanceCounts();

            // Set parameters and execute SQL
            String sql = SELECT_ALL_ATTRIBUTES +
                         "LEFT JOIN `survey` s ON a.`user_id` = s.`user_id` AND a.`event_id` = s.`event_id` " +
                         "WHERE a.`user_id` = ? AND s.`survey_id` IS NULL AND e.`end_date_time` <= NOW()";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getId());
            ResultSet results = preparedStatement.executeQuery();

            // Store results in list
            while (results.next()) {
                Attendance attendance = new Attendance();
                setAttributes(attendance, attendanceCounts, results);
                pendingSurveys.add(attendance);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return pendingSurveys;
    }

    /**
     * Gets a count of the total number of users associated with an event
     * @param eventId The ID of the event whose attendance count to get
     * @return The count of users associated with a single event
     */
    public int getAttendanceCount(int eventId) {
        try {
            String sql = "SELECT COUNT(`event_id`) AS 'count' FROM `event_attendance` WHERE `event_id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, eventId);
            ResultSet results = preparedStatement.executeQuery();

            if (results.next()) {
                return results.getInt("count");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Gets the number of users signed in to an event
     * @param eventId The ID of the event desired
     * @return The count of users for that event whose status is SIGNED_IN or ATTENDED
     */
    public int getSignInCount(int eventId) {
        try {
            String sql = "SELECT COUNT(`event_id`) AS 'count' FROM `event_attendance` " +
                         "WHERE `event_id` = ? AND `attendance_status_id` > 0";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, eventId);
            ResultSet results = preparedStatement.executeQuery();

            if (results.next()) {
                return results.getInt("count");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Gets an attendance count for each event id
     */
    public Map<Integer, Integer> getAttendanceCounts() {
        Map<Integer, Integer> attendanceCounts = new HashMap<Integer, Integer>();

        try {
            String sql = "SELECT DISTINCT `event_id` FROM `event_attendance`";
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            while (results.next()) {
                Integer key = results.getInt("event_id");
                Integer value = getAttendanceCount(key);
                attendanceCounts.put(key, value);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attendanceCounts;

    }

    /**
     * Creates a new event attendance entry in the database (effectively a new registration for a user for an event).
     * Uses default attendance status of NOT_ATTENDED
     * @param user The user to register
     * @param event The event for which to register
     * @return 0 for success, -1 for invalid data, SQL error code for database failure
     */
    public int insertAttendance(User user, Event event) {
        if (!AttendanceHelpers.validate(user, event)) {
            return -1;
        }

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
     * Creates a new event attendance entry in the database with supplied attendance status.
     * For simultaneous registration and sign-in of user.
     * @param user The user to register
     * @param event The event for which to register
     * @param status The status code for the new registration
     * @return 0 for success, -1 for invalid data, SQL error code for database failure
     */
    public int insertAttendance(User user, Event event, int status) {
        if (!AttendanceHelpers.validate(user, event)) {
            return -1;
        }

        try {
            // Set parameters and execute SQL
            String sql = "INSERT INTO `event_attendance`(`user_id`, `event_id`, `attendance_status_id`) " +
                    "VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(2, event.getId());
            preparedStatement.setInt(3, status);
            preparedStatement.executeUpdate();
            return 0;
        } catch (SQLException e) {
            return e.getErrorCode();
        }
    }

    /**
     * Removes the specified attendance entry from the database (effectively de-registering a user from an event)
     * @param attendance The attendance record to remove
     * @return 0 for success, -1 for invalid data, SQL error code for database failure
     */
    public int deleteAttendance(Attendance attendance) {
        try {
            // Set parameters and execute SQL
            String sql = "DELETE FROM `event_attendance` WHERE `user_id` = ? AND event_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, attendance.getUser().getId());
            preparedStatement.setInt(2, attendance.getEvent().getId());
            preparedStatement.executeUpdate();
            return 0;
        } catch (SQLException e) {
            return e.getErrorCode();
        }
    }

    /**
     * Updates an attendance status code for a given user and event
     * @param attendance The record whose status needs to be changed
     * @param status The ordinal status code to set
     * @return 0 for success, -1 for invalid data, SQL error code for database failure
     */
    public int updateStatus(Attendance attendance, int status) {
        if (!AttendanceHelpers.validateStatus(attendance, status)) {
            return -1;
        }

        try {
            // Set parameters and execute SQL
            String sql = "UPDATE `event_attendance` SET `attendance_status_id` = ? " +
                         "WHERE `user_id` = ? AND event_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, status);
            preparedStatement.setInt(2, attendance.getUser().getId());
            preparedStatement.setInt(3, attendance.getEvent().getId());
            preparedStatement.executeUpdate();
            return 0;
        } catch (SQLException e) {
            return e.getErrorCode();
        }
    }

    /**
     * Updates an attendance status code for a given user and event
     * @param event The event for the update
     * @param user The user for the update
     * @param status The ordinal status code to set
     * @return 0 for success, -1 for invalid data, SQL error code for database failure
     */
    public int updateStatus(Event event, User user, int status) {
        Attendance attendance = new Attendance();
        attendance.setEvent(event);
        attendance.setUser(user);

        return updateStatus(attendance, status);
    }

    /**
     * Sets the attributes of an Attendance object based on the result set from a SQL query
     * @param attendance The Attendance object whose attributes to set
     * @param results The results set containing the data
     */
    private void setAttributes(Attendance attendance, Map<Integer, Integer> attendanceCounts, ResultSet results)
            throws SQLException, IllegalArgumentException {
        User user = new User();
        user.setId(results.getInt("user_id"));
        user.setType(User.UserType.valueOf(escapeHtml4(results.getString("user_type").toUpperCase())));
        user.setFirstName(escapeHtml4(results.getString("first_name")));
        user.setLastName(escapeHtml4(results.getString("last_name")));
        user.setEmail(escapeHtml4(results.getString("email")));

        User presenter = new User();
        presenter.setId(results.getInt("presenter_id"));
        presenter.setType(User.UserType.valueOf(escapeHtml4(results.getString("presenter_type").toUpperCase())));
        presenter.setFirstName(escapeHtml4(results.getString("presenter_first_name")));
        presenter.setLastName(escapeHtml4(results.getString("presenter_last_name")));
        presenter.setEmail(escapeHtml4(results.getString("presenter_email")));

        Event event = new Event();
        event.setId(results.getInt("event_id"));
        event.setName(escapeHtml4(results.getString("event_name")));
        event.setStartDateTime(results.getTimestamp("start_date_time").toLocalDateTime());
        event.setEndDateTime(results.getTimestamp("end_date_time").toLocalDateTime());
        event.setPresenter(presenter);
        event.setOpenRegistration(results.getBoolean("open_registration"));
        event.setRegistrationCode(escapeHtml4(results.getString("registration_code")));
        event.setMandatorySurvey(results.getBoolean("mandatory_survey"));
        event.setCapacity(results.getInt("capacity"));

        event.setNumRegistered(attendanceCounts.get(event.getId()));

        attendance.setEvent(event);
        attendance.setUser(user);
        attendance.setStatus(
                Attendance.AttendanceStatus.valueOf(escapeHtml4(results.getString("attendance_status").toUpperCase())));

    }

}
