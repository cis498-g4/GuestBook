<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<!-- TODO: HTML / JS form validation -->
<form action="add-event" method="post">
    <label for="name">Event Name:</label>
    <input type="text" name="name" id="name" required><br>
    <label for="start-date">Start date:</label>
    <input type="text" name="start-date" id="start-date" required><br>
    <label for="start-time">Start time:</label>
    <input type="text" name="start-time" id="start-time" required><br>
    <label for="end-date">End date:</label>
    <input type="text" name="end-date" id="end-date" required><br>
    <label for="end-time">End time:</label>
    <input type="text" name="end-time" id="end-time" required><br>
    <label for="pres-id">Presenter:</label>
    <select name="pres-id" id="pres-id">

        <c:forEach items="${organizers}" var="organizer">
        <option value="${organizer.id}">${organizer.firstName} ${organizer.lastName}</option>
        </c:forEach>

    </select><br>
    <label for="capacity">Event capacity:</label>
    <input type="text" name="capacity" id="capacity"><br>
    <input type="checkbox" name="open-reg" id="open-reg">Allow open registration<br>
    <label for="reg-code">Registration code (optional):</label>
    <input type="text" name="reg-code" id="reg-code"><br>
    <input type="checkbox" name="survey-req" id="survey-req">Require survey completion to record attendance<br>
    <input type="submit" value="create event">
</form>

<hr>

<button onclick="history.go(-1)">back</button>

</body>
</html>

