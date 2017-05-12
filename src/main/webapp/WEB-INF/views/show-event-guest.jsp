<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<div class="row">
    <div class="col-sm-4 col-sm-offset-4">
        <table class="table table-condensed info-list">
            <thead hidden></thead>
            <tbody>
                <tr>
                    <td class="col-xs-6"><strong>Event Name:</strong></td>
                    <td class="col-xs-6">${event.name}</td>
                </tr>
                <tr>
                    <td class="col-xs-6"><strong>Event Start:</strong></td>
                    <td class="col-xs-6">${eventStart}</td>
                </tr>
                <tr>
                    <td class="col-xs-6"><strong>Event End:</strong></td>
                    <td class="col-xs-6">${eventEnd}</td>
                </tr>
                <tr>
                    <td class="col-xs-6"><strong>Presenter:</strong></td>
                    <td class="col-xs-6">${event.presenter.firstName} ${event.presenter.lastName}</td>
                </tr>
                <tr>
                    <td class="col-xs-6"><strong>Registration Code:</strong></td>
                    <td class="col-xs-6">${event.registrationCode != null ? event.registrationCode : "<em>none</em>"}</td>
                </tr>
                <tr>
                    <td class="col-xs-6"><strong>Survey Required:</strong></td>
                    <td class="col-xs-6">${event.mandatorySurvey ? "Yes" : "No"}</td>
                </tr>
            </tbody>
        </table>
    </div>
</div>

<hr>

<div class="text-center">
    <c:if test="${temporalStatus == 'upcoming'}">
        <form action="remove-registration-guest">
            You are registered to attend this event on ${eventLongDate}.
            <input type="hidden" name="eventId" value="${event.id}">
            <input type="submit" class="btn btn-link" value="remove registration">
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
            <input type="submit" class="btn btn-link" value="take survey">
        </form>
    </c:if>
    <c:if test="${temporalStatus == 'in-attendance'}">
        <p>You are signed in to this event.</p>
    </c:if>
    <c:if test="${temporalStatus == 'attended'}">
        <p>You attended this event on ${eventLongDate}.</p>
    </c:if>
</div>

<hr>

<div class="text-center">
    <button class="btn btn-primary" onclick="window.location.href='list-events-guest'">Back</button>
</div>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>

</body>
</html>
