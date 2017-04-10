package com.cis498.group4.data;

import com.cis498.group4.models.Survey;
import com.cis498.group4.util.DbConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The SurveyDataAccess class facilitates operations on Survey data in the database.
 */
public class SurveyDataAccess {

    private Connection connection;

    public SurveyDataAccess(Connection connection) {
        this.connection = DbConn.getConnection();
    }

    public Survey getSurvey(int id) {
        // TODO: Match table and attribute names in DB
        Survey survey = new Survey();

        try {
            // Set id parameter and execute SQL statement
            String sql = "SELECT * FROM survey WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet results = preparedStatement.executeQuery();

            // TODO: Store results in Survey object
            if (results.next()) {

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return survey;
    }

    public List<Survey> getAllSurveys() {
        // TODO: Match table and attribute names in DB
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

    public void updateSurvey(int id, Survey survey) {
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

    public void deleteSurvey(int id) {
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
