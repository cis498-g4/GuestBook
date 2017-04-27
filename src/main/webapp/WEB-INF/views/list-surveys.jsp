<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<!-- TODO only display if filter active -->
<p hidden>Surveys {for|by} this {event|presenter|user} are {very|somewhat} {positive|negative}</p>

<table class="table table-responsive">
    <thead>
        <tr>
            <th>Event</th>
            <th>Presenter</th>
            <th>Event Date</th>
            <th>Reviewer</th>
            <th>Submission Date</th>
            <th></th>
        </tr>
    </thead>

    <tbody>
        <c:if test="${surveys.isEmpty()}">
            <tr>
                <td colspan="6" align="center">No surveys found</td>
            </tr>
        </c:if>

        <c:forEach items="${surveys}" var="survey">
            <tr>
                <td>${survey.event.name}</td>
                <td>${survey.event.presenter.lastName}, ${survey.event.presenter.firstName}</td>
                <td>${survey.event.startDateTime.getMonthValue()}/${survey.event.startDateTime.getDayOfMonth()}/${survey.event.startDateTime.getYear()}</td>
                <td>${survey.user.lastName}, ${survey.user.firstName}</td>
                <td>
                    ${survey.submissionDateTime.getMonthValue()}/${survey.submissionDateTime.getDayOfMonth()}/${survey.submissionDateTime.getYear()}
                    ${survey.submissionDateTime.getHour()}:${survey.submissionDateTime.getMinute() < 10 ? "0" : ""}${survey.submissionDateTime.getMinute()}
                </td>
                <td>
                    <form action="show-survey-info">
                        <input type="hidden" name="id" value="${survey.id}">
                        <input type="submit" class="btn btn-link btn-block" value="view results">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>

</table>

<hr>

<form action="filter-survey">
    <label for="field">Filter by: </label>
    <select name="field" id="field">
        <option>Event Name</option>
        <option>Presenter Last Name</option>
        <option>Event Date</option>
        <option>Reviewer Last Name</option>
        <option>Submission Date</option>
    </select>
    <input type="text" name="value">
    <input type="checkbox" name="exact" checked>Exact matches only
    <input type="submit" class="btn btn-default btn-sm" value="submit">
</form>

<hr>

<td><button onclick="">download CSV</button></td>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>
