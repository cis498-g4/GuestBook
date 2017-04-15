<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/templates/header.jsp"></jsp:include>

<p>The following event will be <strong>permanently</strong> removed:</p>

<table>
    <tr>
        <th>Event Name:</th>
        <td>${event.name}</td>
    </tr>
    <tr>
        <th>Event Start:</th>
        <td>
            ${event.startDateTime.getMonthValue()}/${event.startDateTime.getDayOfMonth()}/${event.startDateTime.getYear()}
            ${event.startDateTime.getHour()}:${event.startDateTime.getMinute() < 10 ? "0" : "&nbsp;"}${event.startDateTime.getMinute()}
        </td>
    </tr>
    <tr>
        <th>Event End:</th>
        <td>
            ${event.endDateTime.getMonthValue()}/${event.endDateTime.getDayOfMonth()}/${event.endDateTime.getYear()}
            ${event.endDateTime.getHour()}:${event.endDateTime.getMinute() < 10 ? "0" : "&nbsp;"}${event.endDateTime.getMinute()}
        </td>
    </tr>
    <tr>
        <th>Presenter:</th>
        <td>${event.presenter.firstName} ${event.presenter.lastName}</td>
    </tr>
    <tr>
        <th>Registration Type:</th>
        <td>${event.openRegistration ? "Open" : "Closed"}</td>
    </tr>
    <tr>
        <th>Registration Code:</th>
        <td>${event.registrationCode != null ? event.registrationCode : "<em>none</em>"}</td>
    </tr>
    <tr>
        <th>Survey Required:</th>
        <td>${event.mandatorySurvey ? "Yes" : "No"}</td>
    </tr>
    <tr>
        <th>Max Capacity:</th>
        <td>${event.capacity > 0 ? event.capacity : "<em>none</em>"}</td>
    </tr>
</table>

<form action="delete-event" method="post">
    <p>
        This effectively cancels the event. Are you sure?<br>
        <input type="hidden" name="id" value="${event.id}"><br>
        <input type="submit" value="confirm delete">
        <button onclick="history.go(-1)">cancel</button>
    </p>
</form>

<hr>

<button onclick="history.go(-1)">back</button>

</body>
</html>


