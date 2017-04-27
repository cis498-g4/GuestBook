<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<table>
    <tr>
        <th>Event Name</th>
        <th>Start</th>
        <th>End</th>
        <th>Presenter</th>
        <th>Registration Type</th>
        <th>Registration Code</th>
        <th>Survey Required</th>
        <th>Max Capacity</th>
    </tr>

    <c:if test="${events.isEmpty()}">
    <tr>
        <td colspan="11" align="center">No events found</td>
    </tr>
    </c:if>

    <c:forEach items="${events}" var="event">
        <tr>
            <td>${event.name}</td>
            <td>
                ${event.startDateTime.getMonthValue()}/${event.startDateTime.getDayOfMonth()}/${event.startDateTime.getYear()}
                ${event.startDateTime.getHour()}:${event.startDateTime.getMinute() < 10 ? "0" : ""}${event.startDateTime.getMinute()}
            </td>
            <td>
                ${event.endDateTime.getMonthValue()}/${event.endDateTime.getDayOfMonth()}/${event.endDateTime.getYear()}
                ${event.endDateTime.getHour()}:${event.endDateTime.getMinute() < 10 ? "0" : ""}${event.endDateTime.getMinute()}
            </td>
            <td>${event.presenter.lastName}, ${event.presenter.firstName}</td>
            <td>${event.openRegistration ? "Open" : "Closed"}</td>
            <td>${event.registrationCode != null ? event.registrationCode : "<em>none</em>"}</td>
            <td>${event.mandatorySurvey ? "Yes" : "No"}</td>
            <td>${event.capacity > 0 ? event.capacity : "<em>none</em>"}</td>

            <td>
                <form action="show-event-info">
                    <input type="hidden" name="id" value="${event.id}">
                    <input type="submit" value="info">
                </form>
            </td>
            <td>
                <form action="update-event">
                    <input type="hidden" name="id" value="${event.id}">
                    <input type="submit" value="update">
                </form>
            </td>
            <td>
                <form action="remove-event">
                    <input type="hidden" name="id" value="${event.id}">
                    <input type="submit" value="remove">
                </form>
            </td>

        </tr>
    </c:forEach>

</table>

<hr>

<form action="filter-event">
    <label for="field">Filter by: </label>
    <select name="field" id="field">
        <option>Event Name</option>
        <option>Start Date/Time</option>
        <option>End Date/Time</option>
        <option>Presenter Last Name</option>
        <option>Registration Type</option>
        <option>Registration Code</option>
        <option>Survey Required</option>
        <option>Max Capacity</option>
    </select>
    <input type="text" name="value">
    <input type="checkbox" name="exact" checked>Exact matches only
    <input type="submit" value="submit">
</form>

<hr>

<a href="add-event">+ Add new event</a>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>
