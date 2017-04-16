<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/templates/header.jsp"></jsp:include>

<!-- TODO only display if filter active -->
<p hidden>Surveys {for|by} this {event|presenter|user} are {very|somewhat} {positive|negative}</p>

<table>
    <tr>
        <th>Event</th>
        <th>Presenter</th>
        <th>Event Date</th>
        <th>Reviewer</th>
        <th>Submission Date</th>
    </tr>

    <c:if test="${surveys.isEmpty()}">
        <tr>
            <td colspan="11" align="center">No surveys found</td>
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
                <form action="view-survey">
                    <input type="hidden" name="id" value="${survey.id}">
                    <input type="submit" value="view">
                </form>
            </td>
        </tr>
    </c:forEach>

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
    <input type="submit" value="submit">
</form>

<hr>

<a href="#">Download as CSV</a>

</body>
</html>
