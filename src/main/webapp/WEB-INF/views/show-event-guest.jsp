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
        <td>${eventStart}</td>
    </tr>
    <tr>
        <td><strong>Event End:</strong></td>
        <td>${eventEnd}</td>
    </tr>
    <tr>
        <td><strong>Presenter:</strong></td>
        <td>${event.presenter.firstName} ${event.presenter.lastName}</td>
    </tr>
    <tr>
        <td><strong>Registration Code:</strong></td>
        <td>${event.registrationCode != null ? event.registrationCode : "<em>none</em>"}</td>
    </tr>
    <tr>
        <td><strong>Survey Required:</strong></td>
        <td>${event.mandatorySurvey ? "Yes" : "No"}</td>
    </tr>
</table>

<hr>

<c:if test="${temporalStatus == 'upcoming'}">
    <p>
        You are registered to attend this event on ${eventLongDate}.
        <form action="remove-registration-guest">
            <input type="hidden" name="eventId" value="${event.id}">
            <input type="submit" value="deregister?">
        </form>
    </p>
</c:if>
<c:if test="${temporalStatus == 'current'}">
    <p>This event is already in progress.</p>
</c:if>
<c:if test="${temporalStatus == 'missed'}">
    <p>It looks like you missed this event...</p>
</c:if>
<c:if test="${temporalStatus == 'signed-in'}">
    <p>You are signed in to this event. Remember to complete your survey!</p>
</c:if>
<c:if test="${temporalStatus == 'survey-pending'}">
    <p>
        You signed in to this event, but still need to complete a survey.
        <form action="take-survey">
            <input type="hidden" name="eventId" value="${event.id}">
            <input type="submit" value="take survey">
        </form>
    </p>
</c:if>
<c:if test="${temporalStatus == 'in-attendance'}">
    <p>You are signed in to this event.</p>
</c:if>
<c:if test="${temporalStatus == 'attended'}">
    <p>You attended this event on ${eventLongDate}.</p>
</c:if>

<hr>

<button onclick="history.go(-1)">back</button>

</body>
</html>
