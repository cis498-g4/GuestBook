<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<table class="table table-condensed info-list">
    <thead hidden></thead>
    <tbody>
        <tr>
            <td class="col-xs-3"><strong>Event Name:</strong></td>
            <td>${event.name}</td>
        </tr>
        <tr>
            <td class="col-xs-3"><strong>Event Start:</strong></td>
            <td>${eventStart}</td>
        </tr>
        <tr>
            <td class="col-xs-3"><strong>Event End:</strong></td>
            <td>${eventEnd}</td>
        </tr>
        <tr>
            <td class="col-xs-3"><strong>Presenter:</strong></td>
            <td>${event.presenter.firstName} ${event.presenter.lastName}</td>
        </tr>
        <tr>
            <td class="col-xs-3"><strong>Registration Code:</strong></td>
            <td>${event.registrationCode != null ? event.registrationCode : "<em>none</em>"}</td>
        </tr>
        <tr>
            <td class="col-xs-3"><strong>Survey Required:</strong></td>
            <td>${event.mandatorySurvey ? "Yes" : "No"}</td>
        </tr>
    </tbody>
</table>

<hr>

<c:if test="${temporalStatus == 'upcoming'}">
    <form action="remove-registration-guest">
        You are registered to attend this event on ${eventLongDate}.
        <input type="hidden" name="eventId" value="${event.id}">
        <input type="submit" value="remove registration" class="btn btn-link">
    </form>
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
    <form action="take-survey">
        You signed in to this event, but still need to complete a survey.
        <input type="hidden" name="eventId" value="${event.id}">
        <input type="submit" value="take survey" class="btn btn-link">
    </form>
</c:if>
<c:if test="${temporalStatus == 'in-attendance'}">
    <p>You are signed in to this event.</p>
</c:if>
<c:if test="${temporalStatus == 'attended'}">
    <p>You attended this event on ${eventLongDate}.</p>
</c:if>

<hr>

<button class="btn btn-primary" onclick="list-events-guest">Back</button>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>
