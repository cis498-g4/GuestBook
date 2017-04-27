<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<table class="table table-responsive">
    <thead>
        <tr>
            <th>Event Name</th>
            <th>Date</th>
            <th>Presenter</th>
            <th>Registration Type</th>
            <th>Registration Code</th>
            <th># Registered</th>
            <th></th>
        </tr>
    </thead>

    <tbody>
        <c:if test="${events.isEmpty()}">
            <tr>
                <td colspan="7" align="center">No events found</td>
            </tr>
        </c:if>

        <c:forEach items="${registrations}" var="registration">
            <tr>
                <td>${registration.event.name}</td>
                <td>
                        ${registration.event.startDateTime.getMonthValue()}/${registration.event.startDateTime.getDayOfMonth()}/${registration.event.startDateTime.getYear()}
                        ${registration.event.startDateTime.getHour()}:${registration.event.startDateTime.getMinute() < 10 ? "0" : ""}${registration.event.startDateTime.getMinute()}
                </td>
                <td>${registration.event.presenter.lastName}, ${registration.event.presenter.firstName}</td>
                <td>${registration.event.openRegistration ? "Open" : "Closed"}</td>
                <td>${registration.event.registrationCode != null ? registration.event.registrationCode : "<em>none</em>"}</td>
                <td>
                        ${registration.event.numRegistered} / ${registration.event.capacity > 0 ? registration.event.capacity : "&#8734;"}
                </td>
                <td>
                    <form action="remove-registration-guest">
                        <input type="hidden" name="eventId" value="${registration.event.id}">
                        <input type="submit" class="btn btn-link btn-block" value="remove registration">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>

</table>

<hr>

<form action="filter-reg">
    <label for="field">Filter by: </label>
    <select name="field" id="field">
        <option>Event Name</option>
        <option>Presenter Last Name</option>
        <option>Event Date</option>
    </select>
    <input type="text" name="value">
    <input type="checkbox" name="exact" checked>Exact matches only
    <input type="submit" class="btn btn-default btn-sm btn-block" value="submit">
</form>

<hr>

<h4>Register for a new event</h4>

<form action="add-registration-guest" method="post">
    <label for="reg-code">If you have the registration code for a new event, enter it here: </label>
    <input type="text" name="reg-code" id="reg-code" required>
    <input type="submit" class="btn btn-success btn-sm" value="register">
    <p>If you do not have a registration code, please contact an event organizer to assist you with registration.</p>
</form>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>
