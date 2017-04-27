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
            <th>Survey Required</th>
            <th></th>
        </tr>
    </thead>

    <tbody>
        <c:if test="${events.isEmpty()}">
            <tr>
                <td colspan="5" align="center">No pending surveys found</td>
            </tr>
        </c:if>

        <c:forEach items="${events}" var="event">
            <tr>
                <td>${event.name}</td>
                <td>${event.startDateTime.getMonthValue()}/${event.startDateTime.getDayOfMonth()}/${event.startDateTime.getYear()}</td>
                <td>${event.presenter.lastName}, ${event.presenter.firstName}</td>
                <td>${event.mandatorySurvey ? "<strong>Yes</strong>" : "No"}</td>
                <td>
                    <form action="take-survey">
                        <input type="hidden" name="eventId" value="${event.id}">
                        <input type="submit" class="btn btn-default btn-sm btn-block" value="take survey">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>

</table>

<hr>

<form action="filter-surveys-user">
    <label for="field">Filter by: </label>
    <select name="field" id="field">
        <option>Event Name</option>
        <option>Presenter Last Name</option>
        <option>Event Date</option>
        <option>Survey Required</option>
    </select>
    <input type="text" name="value">
    <input type="checkbox" name="exact" checked>Exact matches only
    <input type="submit" class="btn btn-default btn-sm" value="submit">
</form>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>
