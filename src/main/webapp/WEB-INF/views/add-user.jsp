<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<!-- TODO: HTML / JS form validation -->
<form action="add-user" method="post">
    <label for="first-name">First Name:</label>
    <input type="text" name="first-name" id="first-name" required><br>
    <label for="last-name">Last Name:</label>
    <input type="text" name="last-name" id="last-name" required><br>
    <label for="email">Email Address:</label>
    <input type="email" name="email" id="email" required><br>
    <label for="type">User type:</label>
    <select name="type" id="type" selected="GUEST">
        <option value="GUEST">Guest</option>
        <option value="ORGANIZER">Organizer</option>
    </select><br>
    <label for="password">Password</label>
    <input type="password" name="password" id="password" required><br>
    <label for="pwd-conf">Retype Password</label>
    <input type="password" name="pwd-conf" id="pwd-conf" required><br>
    <input type="submit" value="create user">
</form>

<hr>

<button onclick="history.go(-1)">back</button>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>
