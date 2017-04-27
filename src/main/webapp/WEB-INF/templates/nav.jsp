<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="home"><img src="${pageContext.request.contextPath}/img/guestbook.png" width="128"></a>
        </div>
            <c:if test="${sessionUser.type == 'ORGANIZER'}">
                <ul class="nav navbar-nav">
                    <li><a href="list-users">Users</a></li>
                    <li><a href="list-events">Events</a></li>
                    <li><a href="list-surveys">Surveys</a></li>
                    <li><a href="list-event-registrations">Registration</a></li>
                    <li><a href="start-kiosk">Sign-In Kiosk</a></li>
                </ul>
            </c:if>
            <c:if test="${sessionUser.type == 'GUEST'}">
                <ul class="nav navbar-nav">
                    <li><a href="list-events-guest">My Events</a></li>
                    <li><a href="list-surveys-guest">My Surveys</a></li>
                    <li><a href="list-registrations-guest">Event Registration</a></li>
                    <li><a href="show-user-info-guest">My Account</a></li>
                </ul>
            </c:if>
            <c:if test="${sessionUser != null}">
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="logout">Logout ${sessionUser.firstName} ${sessionUser.lastName}</a></li>
                </ul>
            </c:if>
    </div>
</nav>
