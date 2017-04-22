<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<% response.setHeader("Refresh", "30;url=/kiosk"); %>

<!-- TODO: HTML / JS form validation -->
<form action="/manager/add-user-kiosk" method="post">
    <label for="first-name">First Name:</label>
    <input type="text" name="first-name" id="first-name" required><br>
    <label for="last-name">Last Name:</label>
    <input type="text" name="last-name" id="last-name" required><br>
    <label for="email">Email Address:</label>
    <input type="email" name="email" id="email" required><br>
    <input type="hidden" name="type" value="GUEST">
    <label for="password">Password</label>
    <input type="password" name="password" id="password" required><br>
    <label for="pwd-conf">Retype Password</label>
    <input type="password" name="pwd-conf" id="pwd-conf" required><br>
    <input type="submit" value="create user">
</form>

</body>
</html>