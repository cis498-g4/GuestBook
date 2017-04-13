<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <title>${pageTitle}</title>
</head>
<body>

<h2>${pageTitle}</h2>

<form action="edit-user" method="post">
    <label for="first-name">First Name:</label>
    <input type="text" name="first-name" id="first-name" value="${user.firstName}"><br>
    <label for="last-name">Last Name:</label>
    <input type="text" name="last-name" id="last-name" value="${user.lastName}"><br>
    <label for="email">Email Address:</label>
    <input type="email" name="email" id="email" value="${user.email}"><br>
    <label for="type">User type:</label>
    <select name="type" id="type" selected="${user.type}">
        <option value="ORGANIZER">Organizer</option>
        <option value="GUEST">Guest</option>
    </select><br>
    <input type="hidden" name="id" value="${user.id}"><br>
    <input type="submit" value="update information">
</form>

<form action="change-password" method="get">
    <p>
        Need to change the password?
        <input type="hidden" name="id" value="${user.id}">
        <input type="submit" value="click here!">
    </p>
</form>

<hr>

<button onclick="history.go(-1)">back</button>

</body>
</html>

