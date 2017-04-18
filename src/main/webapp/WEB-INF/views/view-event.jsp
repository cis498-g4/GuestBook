<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<table>
    <tr>
        <td><strong>Event Name:</strong></td>
        <td>${event.name}</td>
    </tr>
    <tr>
        <td><strong>Event Start:</strong></td>
        <td>
            ${event.startDateTime.getMonthValue()}/${event.startDateTime.getDayOfMonth()}/${event.startDateTime.getYear()}
            ${event.startDateTime.getHour()}:${event.startDateTime.getMinute() < 10 ? "0" : ""}${event.startDateTime.getMinute()}
        </td>
    </tr>
    <tr>
        <td><strong>Event End:</strong></td>
        <td>
            ${event.endDateTime.getMonthValue()}/${event.endDateTime.getDayOfMonth()}/${event.endDateTime.getYear()}
            ${event.endDateTime.getHour()}:${event.endDateTime.getMinute() < 10 ? "0" : ""}${event.endDateTime.getMinute()}
        </td>
    </tr>
    <tr>
        <td><strong>Presenter:</strong></td>
        <td>${event.presenter.firstName} ${event.presenter.lastName}</td>
    </tr>
    <tr>
        <td><strong>Registration Type:</strong></td>
        <td>${event.openRegistration ? "Open" : "Closed"}</td>
    </tr>
    <tr>
        <td><strong>Registration Code:</strong></td>
        <td>${event.registrationCode != null ? event.registrationCode : "<em>none</em>"}</td>
    </tr>
    <tr>
        <td><strong>Survey Required:</strong></td>
        <td>${event.mandatorySurvey ? "Yes" : "No"}</td>
    </tr>
    <tr>
        <td><strong>Max Capacity:</strong></td>
        <td>${event.capacity > 0 ? event.capacity : "<em>none</em>"}</td>
    </tr>
</table>

<hr>

<table>
    <tr>
        <td><button onclick="history.go(-1)">back</button></td>

        <c:forTokens items="edit,delete" delims="," var="action">
            <td>
                <form action="${action}-event">
                    <input type="hidden" name="id" value="${event.id}">
                    <input type="submit" value="${action}">
                </form>
            </td>
        </c:forTokens>

    </tr>
</table>

</body>
</html>
