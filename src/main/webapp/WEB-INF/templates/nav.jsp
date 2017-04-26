<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav>
    <a href="home"><img src="${pageContext.request.contextPath}/img/guestbook.png" width="128"></a>
    <c:if test="${sessionUser.type == 'ORGANIZER'}">
        <a href="list-users">Users</a> |
        <a href="list-events">Events</a> |
        <a href="list-surveys">Surveys</a> |
        <a href="event-reg-list">Registration</a> |
        <a href="start-kiosk">Sign-In Kiosk</a> |
        <a href="logout">Logout</a>
    </c:if>
    <c:if test="${sessionUser.type == 'GUEST'}">
        <a href="list-events-guest">My Events</a> |
        <a href="list-surveys-guest">My Surveys</a> |
        <a href="list-registrations-guest">Event Registration</a> |
        <a href="show-user-info-guest">My Account</a> |
        <a href="logout">Logout</a>
    </c:if>
</nav>

<hr>

