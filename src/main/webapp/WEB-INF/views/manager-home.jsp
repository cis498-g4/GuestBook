<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<c:if test="${sessionUser.type == 'ORGANIZER'}">
    <ul>
        <li><a href="list-users">Manage Users</a></li>
        <li><a href="list-events">Manage Events</a></li>
        <li><a href="list-surveys">View Surveys</a></li>
        <li><a href="event-reg-list">Event Registration</a></li>
        <li><a href="start-kiosk">Sign-In Kiosk</a></li>
        <li><a href="logout">Logout</a></li>
    </ul>
</c:if>

<c:if test="${sessionUser.type == 'GUEST'}">
    <ul>
        <li><a href="#">Option</a></li>
    </ul>
</c:if>

</body>
</html>
