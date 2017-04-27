<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<p>This will <strong>remove</strong> your registration from the event ${event.name} on ${eventLongDate}.</p>
<p>${warningMessage}</p>

<form action="remove-registration-guest" method="post">
    <p>
        Are you sure?<br>
        <input type="hidden" name="eventId" value="${event.id}"><br>
        <input type="submit" value="remove registration">
        <button onclick="history.go(-1)">cancel</button>
    </p>
</form>

<hr>

<button onclick="history.go(-1)">back</button>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>


