package com.cis498.group4.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * The DbConn class handles the database connection to the MySQL server.
 * Connection properties are read from the file, /src/main/java/db.properties.
 * Change the properties in this file to match your MySQL instance.
 */
public class DbConn {

    private static Connection connection = null;

    /**
     * Creates and returns a new connection  to the MySQL instance,
     * or returns the current connection if one exists.
     * @return connection - A connection to the MySQL instance
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Load connection properties from db.properties file
                Properties properties = new Properties();
                InputStream inputStream = DbConn.class.getClassLoader().getResourceAsStream("/db.properties");
                properties.load(inputStream);

                // Create and new connection with properties
                String driver = properties.getProperty("driver");
                String url = properties.getProperty("url");
                String user = properties.getProperty("user");
                String password = properties.getProperty("password");

                Class.forName(driver);
                connection = DriverManager.getConnection(url, user, password);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return connection;

    }

}
