<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/templates/header.jsp"></jsp:include>

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

    <c:forEach items="${events}" var="event">
        <tr>
            <td>${event.name}</td>
            <td>
                ${event.startDateTime.getMonthValue()}/${event.startDateTime.getDayOfMonth()}/${event.startDateTime.getYear()}
                ${event.startDateTime.getHour()}:${event.startDateTime.getMinute() < 10 ? "0" : "&nbsp;"}${event.startDateTime.getMinute()}
            </td>
            <td>
                ${event.endDateTime.getMonthValue()}/${event.endDateTime.getDayOfMonth()}/${event.endDateTime.getYear()}
                ${event.endDateTime.getHour()}:${event.endDateTime.getMinute() < 10 ? "0" : "&nbsp;"}${event.endDateTime.getMinute()}
            </td>
            <td>${event.presenter.lastName}, ${event.presenter.firstName}</td>
            <td>${event.openRegistration ? "Open" : "Closed"}</td>
            <td>${event.registrationCode != null ? event.registrationCode : "<em>none</em>"}</td>
            <td>${event.mandatorySurvey ? "Yes" : "No"}</td>
            <td>${event.capacity > 0 ? event.capacity : "<em>none</em>"}</td>

            <c:forTokens items="view,edit,delete" delims="," var="action">
                <td>
                    <form action="${action}-event">
                        <input type="hidden" name="id" value="${event.id}">
                        <input type="submit" value="${action}">
                    </form>
                </td>
            </c:forTokens>

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

</body>
</html>
