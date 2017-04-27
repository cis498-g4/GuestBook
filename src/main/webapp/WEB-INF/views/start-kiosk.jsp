<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<p>Select an event below to launch the kiosk for that event. This will log you out of your management session and start kiosk mode.</p>

<!-- TODO: HTML / JS form validation -->
<form action="start-kiosk" method="post">
    <label for="eventId">Event:</label>
    <select name="eventId" id="eventId">

        <c:forEach items="${futureEvents}" var="event">
            <option value="${event.id}">
                ${event.startDateTime.getMonthValue()}/${event.startDateTime.getDayOfMonth()}/${event.startDateTime.getYear()} ${event.name}
            </option>
        </c:forEach>

    </select><br>
    <input type="submit" value="launch kiosk">
</form>

<hr>

<button onclick="history.go(-1)">back</button>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>

