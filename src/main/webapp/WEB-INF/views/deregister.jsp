<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<p>This will <strong>remove</strong> the registration for ${user.firstName} ${user.lastName} from the event ${event.name} on ${eventDate}.</p>
<p>After removing the registration, there will be ${event.capacity - event.numRegistered + 1} open seats at this event.</p>

<form action="deregister" method="post">
    <p>
        Are you sure?<br>
        <input type="hidden" name="userId" value="${user.id}"><br>
        <input type="hidden" name="eventId" value="${event.id}"><br>
        <input type="submit" value="remove registration">
        <button onclick="history.go(-1)">cancel</button>
    </p>
</form>

<hr>

<button onclick="history.go(-1)">back</button>

</body>
</html>


