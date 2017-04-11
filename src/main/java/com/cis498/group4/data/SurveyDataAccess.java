package com.cis498.group4.data;

import com.cis498.group4.models.Event;
import com.cis498.group4.models.Survey;
import com.cis498.group4.models.User;
import com.cis498.group4.util.DbConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The SurveyDataAccess class facilitates operations on Survey data in the database.
 */
public class SurveyDataAccess {

    private Connection connection;

    public SurveyDataAccess(Connection connection) {
        this.connection = DbConn.getConnection();
    }

    public Survey getSurvey(int id) {
        Survey survey = new Survey();

        try {
            // Set id parameter and execute SQL statement
            String sql = "SELECT s.survey_id, s.submission_date_time, u.user_id, u.user_type, u.first_name, " +
                    "u.last_name, u.email, u.password, e.event_name, e.start_date_time, e.end_date_time, " +
                    "e.registration_code, e.open_registration, e.capacity, e.event_id, sr.question_number, " +
                    "sr.response FROM survey s INNER JOIN `user` u ON s.user_id = u.user_id INNER JOIN event e ON " +
                    "s.event_id = e.event_id LEFT JOIN survey_response sr ON s.survey_id = sr.survey_id WHERE survey_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet results = preparedStatement.executeQuery();

            // Store results in Survey object
            while (results.next()) {

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return survey;
    }

    public List<Survey> getAllSurveys() {
        ArrayList<Survey> surveys = new ArrayList<Survey>();

        try {
            // Execute SQL statement - no parameters, so no need to prepare
            String sql = "SELECT * FROM survey";
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            // TODO: Store results in List of Surveys
            while (results.next()) {

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return surveys;
    }

    public void insertSurvey(int id) {
        try {
            // TODO: Set parameters and execute SQL
            String sql = "";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSurvey(int id, Survey survey) {
        try {
            // TODO: Set parameters and execute SQL
            String sql = "";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSurvey(int id) {
        try {
            // TODO: Set id parameter and execute SQL
            String sql = "";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the attributes of a User object based on the result set from a SQL query
     * @param survey The Survey whose attributes to set
     * @param results The results set containing the data
     */
    private void setAttributes(Survey survey, ResultSet results) throws SQLException, IllegalArgumentException {
        // TODO: There are two users: the survey user and the presenter!
        User user = new User();
        user.setId(results.getInt("user_id"));
        user.setType(User.UserType.valueOf(results.getString("user_type").toUpperCase()));
        user.setFirstName(results.getString("first_name"));
        user.setLastName(results.getString("last_name"));
        user.setEmail(results.getString("email"));
        user.setPassword(results.getString("password"));    // TODO: Should we get/store password? Password hash?

        Event event = new Event();
        event.setId(results.getInt("event_id"));
        event.setName(results.getString("event_name"));
        event.setStartDateTime(results.getTimestamp("start_date_time").toLocalDateTime());
        event.setEndDateTime(results.getTimestamp("end_date_time").toLocalDateTime());
        event.setPresenter(user);
        event.setRegistrationCode(results.getString("registration_code"));
        event.setOpenRegistration(results.getBoolean("open_registration"));
        event.setCapacity(results.getInt("capacity"));

        survey.setId(results.getInt("survey_id"));
        survey.setUser(user);
        survey.setEvent(event);
        survey.setSubmissionDateTime(results.getTimestamp("submission_date_time").toLocalDateTime());

        String key = results.getString("question");
        Integer value = results.getInt("response");
        survey.getResponses().put(key, value);
        
    }

}
