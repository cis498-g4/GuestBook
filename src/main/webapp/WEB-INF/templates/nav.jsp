<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav>
    <a href="home"><img src="${pageContext.request.contextPath}/img/guestbook.png" width="128"></a>
    <c:if test="${sessionUser.type == 'ORGANIZER'}">
        <a href="list-users">Manage Users</a> |
        <a href="list-events">Manage Events</a> |
        <a href="list-surveys">View Surveys</a> |
        <a href="event-reg-list">Event Registration</a> |
        <a href="kiosk">Sign-In Kiosk</a> |
        <a href="logout">Logout</a>
    </c:if>
    <c:if test="${sessionUser.type == 'GUEST'}">
        <a href="#"></a> |
        <a href="logout">Logout</a>
    </c:if>
</nav>

<hr>

