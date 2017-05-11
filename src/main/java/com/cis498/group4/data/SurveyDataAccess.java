package com.cis498.group4.data;

import com.cis498.group4.models.Event;
import com.cis498.group4.models.Survey;
import com.cis498.group4.models.User;
import com.cis498.group4.util.DbConn;
import com.cis498.group4.util.SurveyHelpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

/**
 * The SurveyDataAccess class facilitates operations on Survey data in the database.
 */
public class SurveyDataAccess {

    private Connection connection;

    private final String SELECT_ALL_ATTRIBUTES = "SELECT s.`survey_id`, u.`user_id`, ut.`user_type`, u.`first_name`, " +
            "u.`last_name`, u.`email`, e.`event_id`, e.`event_name`, e.`start_date_time`, e.`end_date_time`, " +
            "p.`user_id` AS 'presenter_id', pt.`user_type` AS 'presenter_type', " +
            "p.`first_name` AS 'presenter_first_name', p.`last_name` AS 'presenter_last_name', " +
            "p.`email` AS 'presenter_email', e.`open_registration`, e.`registration_code`, e.`mandatory_survey`, " +
            "e.`capacity`, s.`submission_date_time`, s.`response_01`, s.`response_02`, s.`response_03`, " +
            "s.`response_04`, s.`response_05`, s.`response_06`, s.`response_07`, s.`response_08`, s.`response_09`, " +
            "s.`response_10` " +
            "FROM `survey` s " +
            "INNER JOIN `user` u ON s.`user_id` = u.`user_id` " +
            "INNER JOIN `user_type` ut ON u.`user_type_id` = ut.`user_type_id` " +
            "INNER JOIN `event` e ON s.`event_id` = e.`event_id` " +
            "INNER JOIN `user` p ON e.`presenter_id` = p.`user_id` " +
            "INNER JOIN `user_type` pt ON p.`user_type_id` = pt.`user_type_id`";

    public SurveyDataAccess() {
        this.connection = DbConn.getConnection();
    }

    /**
     * Retrieves a single row from the `survey` table in the database
     * @param id The ID of the row to retrieve
     * @return Survey object with the data from the row
     */
    public Survey getSurvey(int id) {
        Survey survey = new Survey();

        try {
            // Set id parameter and execute SQL statement
            String sql = SELECT_ALL_ATTRIBUTES + " WHERE s.`survey_id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet results = preparedStatement.executeQuery();

            // Store results in Survey object
            if (results.next()) {
                setAttributes(survey, results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return survey;
    }

    /**
     * Retrieves all rows from `survey` table in the database, as well as their associated responses
     * @return List of Survey objects
     */
    public List<Survey> getAllSurveys() {
        ArrayList<Survey> surveys = new ArrayList<Survey>();

        try {
            // Execute SQL statement - no parameters, so no need to prepare
            String sql = SELECT_ALL_ATTRIBUTES;
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            // Store results in List of Surveys
            while (results.next()) {
                Survey survey = new Survey();
                setAttributes(survey, results);
                surveys.add(survey);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return surveys;
    }

    /**
     * Gets the ID of a survey based on its unique user, event combination
     * @param user The user of the survey
     * @param event The event for the survey
     * @return The id of the survey, or 0 if not found, or -1 if exception occurs
     */
    public int getSurveyId(User user, Event event){
        try {
            String sql = "SELECT `survey_id` FROM `survey` WHERE `user_id` = ? AND `event_id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(2, event.getId());
            ResultSet results = preparedStatement.executeQuery();

            if (results.next()) {
                return results.getInt("survey_id");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            return -1;
        }
    }

    /**
     * Inserts a new survey with responses into the database
     * @param survey The Survey to insert.
     * @return 0 for success, -1 for invalid data, SQL error code for database failure
     */
    public int insertSurvey(Survey survey) {
        if (!SurveyHelpers.validate(survey)) {
            return -1;
        }

        try {
            // Set parameters and execute SQL
            String sql = "INSERT INTO `survey`(`user_id`, `event_id`, `submission_date_time`, `response_01`, " +
                         "`response_02`, `response_03`, `response_04`, `response_05`, `response_06`, `response_07`, " +
                         "`response_08`, `response_09`, `response_10`) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement surveyPstmt = connection.prepareStatement(sql);
            surveyPstmt.setInt(1, survey.getUser().getId());
            surveyPstmt.setInt(2, survey.getEvent().getId());
            surveyPstmt.setString(3,
                    survey.getSubmissionDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            Map<String, Integer> responses = survey.getResponses();

            for (int i = 1; i <= 10; i ++) {
                String key = String.format("response_%02d", i);
                surveyPstmt.setInt(3 + i, responses.get(key));
            }

            surveyPstmt.executeUpdate();
            return 0;
        } catch (SQLException e) {
            return e.getErrorCode();
        }
    }

    /**
     * Sets the attributes of a Survey object based on the result set from a SQL query
     * @param survey The Survey whose attributes to set
     * @param results The results set containing the data
     */
    private void setAttributes(Survey survey, ResultSet results) throws SQLException, IllegalArgumentException {
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

        survey.setId(results.getInt("survey_id"));
        survey.setUser(user);
        survey.setEvent(event);
        survey.setSubmissionDateTime(results.getTimestamp("submission_date_time").toLocalDateTime());

        Map<String, Integer> responses = new HashMap<String, Integer>();

        for(int i = 1; i <= 10; i++) {
            String responseLabel = String.format("response_%02d", i);
            responses.put(responseLabel, results.getInt(responseLabel));
        }

        survey.setResponses(responses);

    }

}
