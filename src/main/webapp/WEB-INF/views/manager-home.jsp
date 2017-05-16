<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<c:if test="${sessionUser.type == 'ORGANIZER'}">
    <div class="row text-center">
        <div class="col-sm-4"><a class="btn btn-default btn-block" style="margin-bottom: 16px;" href="list-users">Manage Users</a></div>
        <div class="col-sm-4"><a class="btn btn-default btn-block" style="margin-bottom: 16px;" href="list-events">Manage Events</a></div>
        <div class="col-sm-4"><a class="btn btn-default btn-block" style="margin-bottom: 16px;" href="list-surveys">View Surveys</a></div>
    </div>
    <div class="row text-center">
        <div class="col-sm-4"><a class="btn btn-default btn-block" style="margin-bottom: 16px;" href="list-event-registrations">Event Registration</a></div>
        <div class="col-sm-4"><a class="btn btn-default btn-block" style="margin-bottom: 16px;" href="start-kiosk">Sign-In Kiosk</a></div>
        <div class="col-sm-4"><a class="btn btn-default btn-block" style="margin-bottom: 16px;" href="logout">Logout</a></div>
    </div>
</c:if>

<c:if test="${sessionUser.type == 'GUEST'}">
    <div class="row text-center">
        <div class="col-sm-4"><a class="btn btn-default btn-block" style="margin-bottom: 16px;" href="list-events-guest">My Events</a></div>
        <div class="col-sm-4"><a class="btn btn-default btn-block" style="margin-bottom: 16px;" href="list-surveys-guest">My Surveys</a></div>
        <div class="col-sm-4"><a class="btn btn-default btn-block" style="margin-bottom: 16px;" href="list-registrations-guest">Event Registration</a></div>
    </div>
    <div class="row text-center">
        <div class="col-sm-4 col-sm-offset-2"><a class="btn btn-default btn-block" style="margin-bottom: 16px;" href="show-user-info-guest">My Account</a></div>
        <div class="col-sm-4"><a class="btn btn-default btn-block" style="margin-bottom: 16px;" href="logout">Logout</a></div>
    </div>
</c:if>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>

</body>
</html>
