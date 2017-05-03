<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<div class="spacer_1em"></div>

<div class="text-center">
    <h3>Single Registration</h3>
    <p>Register an individual user for Coding in Java by entering their email address below.</p>
</div>

<!-- TODO: HTML / JS form validation -->
<div class="row padding-horiz-10px">
    <div class="col-sm-6 col-sm-offset-3">
        <form class="form-vertical" action="add-registration" method="post">

            <div class="form-group text-center">
                <input type="email" class="form-control" name="email" id="email" placeholder="Email address" required>
            </div>

            <div class="form-group text-center">
                <input type="hidden" name="eventId" value="${event.id}">
                <input type="submit" class="btn btn-success" value="Register ${statusType.equals('success') ? 'Another' : ''} User">
            </div>

        </form>
    </div>
</div>

<hr>

<div class="text-center">
    <h3>Group Registration</h3>
    <p>Register multiple users simulataneously by uploading a CSV in the format last name, first name, email address</p>

    <button class="btn btn-success" onclick="#">Upload CSV</button>
</div>

<hr>

<div class="text-center">
    <a class="btn btn-primary" href="list-user-regs-for-event?id=${event.id}">Registration List for ${event.name}</a>
</div>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>
