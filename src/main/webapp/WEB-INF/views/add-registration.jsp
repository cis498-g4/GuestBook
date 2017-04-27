<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<p>Register a user for ${event.name} by entering their email address below.</p>

<!-- TODO: HTML / JS form validation -->
<form action="add-registration" method="post">
    <label for="email">Email:</label>
    <input type="email" name="email" id="email" required><br>
    <input type="hidden" name="eventId" value="${event.id}">
    <input type="submit" value="register user">
</form>

<hr>

<p>Or, upload a CSV to register multiple users at once. CSV must be in the format last name, first name, email address</p>
<button onclick="#">upload CSV</button>

<hr>

<button onclick="history.go(-1)">back</button>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>
