package com.cis498.group4.data;

import com.cis498.group4.models.Event;
import com.cis498.group4.models.Survey;
import com.cis498.group4.models.User;

import java.time.LocalDateTime;
import java.util.*;

/**
 * The TestData class contains sample data to drive and test the application without a database connection.
 */
public final class TestData {

    private static List<User> users = new ArrayList<User>();
    private static List<Event> events = new ArrayList<Event>();
    private static List<Survey> surveys = new ArrayList<Survey>();

    private TestData() {
    }

    public static List<User> getUsers() {
        users.add(new User(1, User.UserType.ORGANIZER, "Perwaize", "Ahmed", "PerwaizeAhmed2013@u.northwestern.edu", "cis498"));
        users.add(new User(2, User.UserType.ORGANIZER, "Matt", "Granum", "MatthewGranum2017@u.northwestern.edu", "cis498"));
        users.add(new User(3, User.UserType.ORGANIZER, "Scott", "Langnas", "ScottLangnas2018@u.northwestern.edu", "cis498"));
        users.add(new User(4, User.UserType.ORGANIZER, "Mike", "Molenda", "m-molenda@northwestern.edu", "cis498"));
        users.add(new User(5, User.UserType.GUEST, "Albert", "Einstein", "albert@cis498.com", "cis498"));
        users.add(new User(6, User.UserType.GUEST, "Marie", "Curie", "marie@cis498.com", "cis498"));
        users.add(new User(7, User.UserType.GUEST, "Isaac", "Newton", "isaac@cis498.com", "cis498"));
        users.add(new User(8, User.UserType.GUEST, "Stephen", "Hawking", "stephen@cis498.com", "cis498"));
        return users;
    }

    public static List<Event> getEvents() {
        events.add(new Event(1, "Building Webpages", LocalDateTime.parse("2017-07-01T10:00:00"), LocalDateTime.parse("2017-07-01T11:00:00"), TestData.getUsers().get(1), "ABC123", true, 100));
        events.add(new Event(2, "Web App Hosting", LocalDateTime.parse("2017-07-02T10:00:00"), LocalDateTime.parse("2017-07-02T11:00:00"), TestData.getUsers().get(2), "ABC123", true, 75));
        events.add(new Event(3, "MySQL Databases", LocalDateTime.parse("2017-07-01T13:00:00"), LocalDateTime.parse("2017-07-01T14:00:00"), TestData.getUsers().get(3), "ABC123", false, 25));
        events.add(new Event(4, "Coding in Java", LocalDateTime.parse("2017-07-02T13:00:00"), LocalDateTime.parse("2017-07-02T14:00:00"), TestData.getUsers().get(4), "ABC123", false, 20));
        return events;
    }

    public static List<Survey> getSurveys() {
        surveys.add(new Survey(1, TestData.getUsers().get(5), TestData.getEvents().get(1), LocalDateTime.parse("2017-07-03T12:00:00"), Arrays.asList(new Integer[]{5, 6, 4, 9, 7, 9, 8, 4, 5, 6})));
        surveys.add(new Survey(2, TestData.getUsers().get(6), TestData.getEvents().get(1), LocalDateTime.parse("2017-07-05T12:00:00"), Arrays.asList(new Integer[]{2, 9, 4, 3, 9, 9, 4, 5, 8, 8})));
        surveys.add(new Survey(3, TestData.getUsers().get(7), TestData.getEvents().get(2), LocalDateTime.parse("2017-07-03T14:00:00"), Arrays.asList(new Integer[]{9, 9, 9, 9, 7, 9, 8, 7, 9, 9})));
        surveys.add(new Survey(4, TestData.getUsers().get(5), TestData.getEvents().get(3), LocalDateTime.parse("2017-07-06T12:00:00"), Arrays.asList(new Integer[]{2, 3, 4, 8, 8, 8, 8, 5, 7, 5})));
        return surveys;
    }
}
