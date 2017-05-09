<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<c:if test="${sessionUser.type == 'ORGANIZER'}">
    <ul>
        <li><a href="list-users">Manage Users</a></li>
        <li><a href="list-events">Manage Events</a></li>
        <li><a href="list-surveys">View Surveys</a></li>
        <li><a href="list-event-registrations">Event Registration</a></li>
        <li><a href="start-kiosk">Sign-In Kiosk</a></li>
        <li><a href="logout">Logout</a></li>
    </ul>
</c:if>

<c:if test="${sessionUser.type == 'GUEST'}">
    <ul>
        <li><a href="list-events-guest">My Events</a></li>
        <li><a href="list-surveys-guest">My Surveys</a></li>
        <li><a href="list-registrations-guest">Event Registration</a></li>
        <li><a href="show-user-info-guest">My Account</a></li>
        <li><a href="logout">Logout</a></li>
    </ul>
</c:if>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>

</body>
</html>
