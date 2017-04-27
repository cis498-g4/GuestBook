<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<!-- TODO: HTML / JS form validation -->
<form action="update-user" method="post">
    <label for="first-name">First Name:</label>
    <input type="text" name="first-name" id="first-name" value="${user.firstName}" required><br>
    <label for="last-name">Last Name:</label>
    <input type="text" name="last-name" id="last-name" value="${user.lastName}" required><br>
    <label for="email">Email Address:</label>
    <input type="email" name="email" id="email" value="${user.email}" required><br>
    <label for="type">User type:</label>
    <select name="type" id="type">
        <option value="GUEST" ${user.type == "GUEST" ? "selected" : ""}>
            Guest
        </option>
        <option value="ORGANIZER" ${user.type == "ORGANIZER" ? "selected" : ""}>
            Organizer
        </option>
    </select><br>
    <input type="hidden" name="id" value="${user.id}"><br>
    <input type="submit" value="update information">
</form>

<h4>Click below to update this user's password</h4>
<form action="update-password" method="get">
    <input type="hidden" name="id" value="${user.id}">
    <input type="submit" id="submit-button" value="change password">
</form>

<hr>

<button onclick="history.go(-1)">back</button>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>
