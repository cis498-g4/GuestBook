package com.cis498.group4.data;

import com.cis498.group4.models.Event;
import com.cis498.group4.models.Survey;
import com.cis498.group4.models.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The TestData class contains sample data to drive and test the application without a database connection.
 */
public final class TestData {

    private static Map<Integer, User> users = new HashMap<Integer, User>();
    private static Map<Integer, Event> events = new HashMap<Integer, Event>();
    private static Map<Integer, Survey> surveys = new HashMap<Integer, Survey>();

    private TestData() {
    }

    public static Map<Integer, User> getUsers() {
        users.put(1, new User(1, User.UserType.ORGANIZER, "Perwaize", "Ahmed", "PerwaizeAhmed2013@u.northwestern.edu", "cis498"));
        users.put(2, new User(2, User.UserType.ORGANIZER, "Matt", "Granum", "MatthewGranum2017@u.northwestern.edu", "cis498"));
        users.put(3, new User(3, User.UserType.ORGANIZER, "Scott", "Langnas", "ScottLangnas2018@u.northwestern.edu", "cis498"));
        users.put(4, new User(4, User.UserType.ORGANIZER, "Mike", "Molenda", "m-molenda@northwestern.edu", "cis498"));
        users.put(5, new User(5, User.UserType.GUEST, "Albert", "Einstein", "albert@cis498.com", "cis498"));
        users.put(6, new User(6, User.UserType.GUEST, "Marie", "Curie", "marie@cis498.com", "cis498"));
        users.put(7, new User(7, User.UserType.GUEST, "Isaac", "Newton", "isaac@cis498.com", "cis498"));
        users.put(8, new User(8, User.UserType.GUEST, "Stephen", "Hawking", "stephen@cis498.com", "cis498"));
        return users;
    }

    public static Map<Integer, Event> getEvents() {
        events.put(1, new Event(1, "Building Webpages", LocalDateTime.parse("2017-07-01T10:00:00"), LocalDateTime.parse("2017-07-01T11:00:00"), TestData.getUsers().get(1), "ABC123", true, 100));
        events.put(2, new Event(2, "Web App Hosting", LocalDateTime.parse("2017-07-02T10:00:00"), LocalDateTime.parse("2017-07-02T11:00:00"), TestData.getUsers().get(2), "ABC123", true, 75));
        events.put(3, new Event(3, "MySQL Databases", LocalDateTime.parse("2017-07-01T13:00:00"), LocalDateTime.parse("2017-07-01T14:00:00"), TestData.getUsers().get(3), "ABC123", false, 25));
        events.put(4, new Event(4, "Coding in Java", LocalDateTime.parse("2017-07-02T13:00:00"), LocalDateTime.parse("2017-07-02T14:00:00"), TestData.getUsers().get(4), "ABC123", false, 20));
        return events;
    }

    public static Map<Integer, Survey> getSurveys() {
        surveys.put(1, new Survey(1, TestData.getUsers().get(5), TestData.getEvents().get(1), LocalDateTime.parse("2017-07-03T12:00:00"), Arrays.asList(new Integer[]{5, 6, 4, 9, 7, 9, 8, 4, 5, 6})));
        surveys.put(2, new Survey(2, TestData.getUsers().get(6), TestData.getEvents().get(1), LocalDateTime.parse("2017-07-05T12:00:00"), Arrays.asList(new Integer[]{2, 9, 4, 3, 9, 9, 4, 5, 8, 8})));
        surveys.put(3, new Survey(3, TestData.getUsers().get(7), TestData.getEvents().get(2), LocalDateTime.parse("2017-07-03T14:00:00"), Arrays.asList(new Integer[]{9, 9, 9, 9, 7, 9, 8, 7, 9, 9})));
        surveys.put(4, new Survey(4, TestData.getUsers().get(5), TestData.getEvents().get(3), LocalDateTime.parse("2017-07-06T12:00:00"), Arrays.asList(new Integer[]{2, 3, 4, 8, 8, 8, 8, 5, 7, 5})));
        return surveys;
    }
}
