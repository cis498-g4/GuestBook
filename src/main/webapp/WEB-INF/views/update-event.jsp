<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<!-- TODO: HTML / JS form validation -->
<form action="update-event" method="post">
    <label for="name">Event Name:</label>
    <input type="text" name="name" id="name" value="${event.name}" required><br>
    <label for="start-date">Start date:</label>
    <input type="text" name="start-date" id="start-date" value="${event.startDateTime.getYear()}-${event.startDateTime.getMonthValue() < 10 ? "0" : ""}${event.startDateTime.getMonthValue()}-${event.startDateTime.getDayOfMonth() < 10 ? "0" : ""}${event.startDateTime.getDayOfMonth()}" required><br>
    <label for="start-time">Start time:</label>
    <input type="text" name="start-time" id="start-time" value="${event.startDateTime.getHour() < 10 ? "0" : ""}${event.startDateTime.getHour()}:${event.startDateTime.getMinute() < 10 ? "0" : ""}${event.startDateTime.getMinute()}" required><br>
    <label for="end-date">End date:</label>
    <input type="text" name="end-date" id="end-date" value="${event.endDateTime.getYear()}-${event.endDateTime.getMonthValue() < 10 ? "0" : ""}${event.endDateTime.getMonthValue()}-${event.endDateTime.getDayOfMonth() < 10 ? "0" : ""}${event.endDateTime.getDayOfMonth()}" required><br>
    <label for="end-time">End time:</label>
    <input type="text" name="end-time" id="end-time" value="${event.endDateTime.getHour() < 10 ? "0" : ""}${event.endDateTime.getHour()}:${event.endDateTime.getMinute() < 10 ? "0" : ""}${event.endDateTime.getMinute()}" required><br>
    <label for="pres-id">Presenter:</label>
    <select name="pres-id" id="pres-id">

        <c:forEach items="${organizers}" var="organizer">
            <option value="${organizer.id}" ${event.presenter.id == organizer.id ? "selected" : ""}>
                    ${organizer.firstName} ${organizer.lastName}
            </option>
        </c:forEach>

    </select><br>
    <label for="capacity">Event capacity:</label>
    <input type="text" name="capacity" id="capacity" value="${event.capacity > 0 ? event.capacity : ""}"><br>
    <input type="checkbox" name="open-reg" id="open-reg" ${event.openRegistration ? "checked" : ""}>Allow open registration<br>
    <label for="reg-code">Registration code (optional):</label>
    <input type="text" name="reg-code" id="reg-code" value="${event.registrationCode != null ? event.registrationCode : ""}"><br>
    <input type="checkbox" name="survey-req" id="survey-req" ${event.mandatorySurvey ? "checked" : ""}>Require survey completion to record attendance<br>
    <input type="hidden" name="id" value="${event.id}"><br>
    <input type="submit" value="update event">
</form>

<hr>

<button onclick="history.go(-1)">back</button>

</body>
</html>

