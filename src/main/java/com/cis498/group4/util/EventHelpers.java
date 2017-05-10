package com.cis498.group4.util;

import com.cis498.group4.data.EventDataAccess;
import com.cis498.group4.data.UserDataAccess;
import com.cis498.group4.models.Event;
import com.cis498.group4.models.User;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The EventHelpers class contains methods to assist with Event data (verification, etc)
 */
public class EventHelpers {

    // Event status codes
    public static final int SUCCESSFUL_WRITE = 0;
    public static final int INVALID_DATA = 1;
    public static final int INVALID_DATE = 2;
    public static final int CONCLUDED = 3;
    public static final int START_IN_PAST = 4;
    public static final int END_BEFORE_START = 5;
    public static final int INVALID_PRESENTER = 6;
    public static final int OVERLAPPING_PRESENTER = 7;
    public static final int INVALID_CAPACITY = 8;
    public static final int INVALID_CODE = 9;

    /**
     * Validates an existing event record.
     * Event is not null, has valid id, name, chronological start date and end date in the future,
     * presenter with no conflicts, registration code, and capacity
     * Use before writing to database.
     * @param event
     * @return
     */
    public static boolean validateRecord(Event event) {
        if (event == null) {
            return false;
        }

        if (event.getId() < 1) {
            return false;
        }

        return (validateFields(event));
    }

    /**
     * Validates basic event fields.
     * Event is not null, has valid name, chronological start date and end date in the future, presenter,
     * registration code, and capacity
     * Use before writing new event to database.
     * @param event
     * @return
     */
    public static boolean validateFields(Event event) {
        if (event == null) {
            return false;
        }

        //TODO

        boolean name = validateName(event.getName());
        boolean startInFuture = startsInFuture(event);
        boolean chronological = event.getEndDateTime().isAfter(event.getStartDateTime());
        boolean presenter = validatePresenter(event.getPresenter());
        boolean registrationCode = validateRegistrationCode(event.getRegistrationCode());
        boolean capacity = validateCapacity(event.getCapacity());

        return (name && startInFuture && chronological && presenter && registrationCode && capacity);
    }

    /**
     * Verifies that the capacity is between 1 and 1000, inclusive, or -1
     * @param capacity
     * @return
     */
    public static boolean validateCapacity(int capacity) {
        if (capacity == -1) {
            return true;
        }

        if (capacity >= 1 && capacity <= 1000) {
            return true;
        }

        return false;
    }

    /**
     * Validates event name (is under 256 characters)
     * @param name
     * @return
     */
    public static boolean validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        return name.trim().length() <= 256;
    }

    /**
     * Validates that presenter is not null and of type Organizer
     * @param presenter
     * @return
     */
    public static boolean validatePresenter(User presenter) {
        if (presenter == null) {
            return false;
        }

        return (presenter.getType() == User.UserType.ORGANIZER);
    }

    /**
     * Verifies that the registration code is exactly eight alphanumeric characters, or is null
     * @param code
     */
    public static boolean validateRegistrationCode(String code) {
        if (code == null) {
            return true;
        }

        Pattern pattern = Pattern.compile("^[A-Za-z0-9]{8}$");
        Matcher matcher = pattern.matcher(code);

        return matcher.matches();
    }

    /**
     * Checks whether the event overlaps with an existing event with the same presenter
     * @param eventA
     * @param presenterEvents
     * @return
     */
    public static boolean isOverlapping(Event eventA, List<Event> presenterEvents) {
        if (presenterEvents.isEmpty()) {
            return false;
        }

        for (Event eventB : presenterEvents) {
            if (eventA.getId() != eventB.getId()) {

                if (eventA.getStartDateTime().isEqual(eventB.getStartDateTime())) {
                    return true;
                }

                if (eventA.getEndDateTime().isEqual(eventB.getEndDateTime())) {
                    return true;
                }

                if (eventA.getStartDateTime().isAfter(eventB.getStartDateTime()) &&
                        eventA.getStartDateTime().isBefore(eventB.getEndDateTime())) {
                    return true;
                }

                if (eventA.getEndDateTime().isAfter(eventB.getStartDateTime()) &&
                        eventA.getEndDateTime().isBefore(eventB.getEndDateTime())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks whether an event's start time is in the future
     * @param event
     * @return True if the event's start time is after now
     */
    public static boolean startsInFuture(Event event) {

        if (event.getStartDateTime().isAfter(LocalDateTime.now())) {
            return true;
        }

        return false;
    }

    /**
     * Checks whether an event's end time is in the future
     * @param event
     * @return True if the event's end time is after now
     */
    public static boolean endsInFuture(Event event) {

        if (event.getEndDateTime().isAfter(LocalDateTime.now())) {
            return true;
        }

        return false;
    }

    /**
     * Checks whether an event's start time is in the past
     * @param event
     * @return True if the event's start time is before now
     */
    public static boolean startedInPast(Event event) {

        if (event.getStartDateTime().isBefore(LocalDateTime.now())) {
            return true;
        }

        return false;
    }

    /**
     * Checks whether an event's end time is in the past
     * @param event
     * @return True if the event's end time is before now
     */
    public static boolean endedInPast(Event event) {

        if (event.getEndDateTime().isBefore(LocalDateTime.now())) {
            return true;
        }

        return false;
    }

    /**
     * Checks whther the event is currently in progress, based on its scheduled start and end times
     * @param event
     * @return
     */
    public static boolean isInProgress(Event event) {

        if (startedInPast(event) && endsInFuture(event)) {
            return true;
        }

        return false;
    }

    /**
     * Calculates the number of seconds until the event's end
     * @param event
     * @return Number of seconds until event end, or INT_MAX
     */
    public static int secondsToEnd(Event event) {
        long seconds = LocalDateTime.now().until(event.getEndDateTime(), ChronoUnit.SECONDS);

        if (seconds < Integer.MAX_VALUE) {
            return (int) seconds;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    /**
     * Sets an event object's attributes based on parameters passed in request
     * @param event
     * @param request
     * @param eventData
     * @param userData
     * @return writeStatus of created event
     */
    public static int setAttributesFromRequest(
            Event event, HttpServletRequest request, EventDataAccess eventData, UserDataAccess userData) {

        try {

            event.setName(request.getParameter("name"));

            String startInput = request.getParameter("start-dt");
            LocalDateTime startDt = LocalDateTime.parse(startInput, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            event.setStartDateTime(startDt);

            String endInput = request.getParameter("end-dt");
            LocalDateTime endDt = LocalDateTime.parse(endInput, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            event.setEndDateTime(endDt);

            User presenter = userData.getUser(Integer.parseInt(request.getParameter("pres-id")));
            event.setPresenter(presenter);

            // Get list of events for the presenter, to check for overlaps
            List<Event> presenterEvents = eventData.getPresenterFutureEvents(presenter);

            event.setOpenRegistration(request.getParameter("open-reg") != null);

            if (request.getParameter("reg-code") != null && request.getParameter("reg-code").length() > 0) {
                event.setRegistrationCode(request.getParameter("reg-code"));
            }

            event.setMandatorySurvey(request.getParameter("survey-req") != null);

            if (request.getParameter("capacity") != null && request.getParameter("capacity").length() > 0) {
                event.setCapacity(Integer.parseInt(request.getParameter("capacity")));
            } else {
                event.setCapacity(-1);
            }

            if (event.getCapacity() <= 0) {
                event.setCapacity(-1);
            }

            // Get status message
            return writeStatus(event, presenterEvents);

        } catch (DateTimeParseException e) {
            return EventHelpers.INVALID_DATE;
        } catch (Exception e) {
            return EventHelpers.INVALID_DATA;
        }

    }

    /**
     * Get status code for verifying the success or failure of an insert or update operation
     * @param event
     * @return
     */
    public static int writeStatus(Event event, List<Event> presenterEvents) {

        if (startedInPast(event)) {
            return START_IN_PAST;
        }

        if (event.getEndDateTime().isBefore(event.getStartDateTime())) {
            return END_BEFORE_START;
        }

        if (endedInPast(event)) {
            return CONCLUDED;
        }

        if (!UserHelpers.validateRecord(event.getPresenter())) {
            return INVALID_PRESENTER;
        }

        if (!(event.getPresenter().getType() == User.UserType.ORGANIZER)) {
            return INVALID_PRESENTER;
        }

        if (isOverlapping(event, presenterEvents)) {
            return OVERLAPPING_PRESENTER;
        }

        if (!validateCapacity(event.getCapacity())) {
            return INVALID_CAPACITY;
        }

        if (!validateRegistrationCode(event.getRegistrationCode())) {
            return INVALID_CODE;
        }

        return SUCCESSFUL_WRITE;
    }

}
