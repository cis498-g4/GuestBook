<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>



<table>
    <tr>
        <th>Event Name</th>
        <th>Start</th>
        <th>Presenter</th>
        <th>Status</th>
    </tr>

    <c:if test="${userAttendance.isEmpty()}">
        <tr>
            <td colspan="11" align="center">No events found</td>
        </tr>
    </c:if>

    <c:forEach items="${userAttendance}" var="attendance">
        <tr>
            <td>${attendance.event.name}</td>
            <td>
                ${attendance.event.startDateTime.getMonthValue()}/${attendance.event.startDateTime.getDayOfMonth()}/${attendance.event.startDateTime.getYear()}
                ${attendance.event.startDateTime.getHour()}:${attendance.event.startDateTime.getMinute() < 10 ? "0" : ""}${attendance.event.startDateTime.getMinute()}
            </td>
            <td>${attendance.event.presenter.lastName}, ${attendance.event.presenter.firstName}</td>
            <td>
                <c:if test="${attendance.status == 'NOT_ATTENDED'}">Not attended</c:if>
                <c:if test="${attendance.status == 'SIGNED_IN'}">Signed in</c:if>
                <c:if test="${attendance.status == 'ATTENDED'}">Attended</c:if>
            </td>
            <td>
                <form action="show-event-guest">
                    <input type="hidden" name="id" value="${attendance.event.id}">
                    <input type="submit" value="view details">
                </form>
            </td>
        </tr>
    </c:forEach>

</table>

<hr>

<form action="filter-event-guest">
    <label for="field">Filter by: </label>
    <select name="field" id="field">
        <option>Event Name</option>
        <option>Start Date/Time</option>
        <option>Presenter Last Name</option>
        <option>Attendance status</option>
    </select>
    <input type="text" name="value">
    <input type="checkbox" name="exact" checked>Exact matches only
    <input type="submit" value="submit">
</form>

<hr>

</body>
</html>