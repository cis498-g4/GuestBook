<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/templates/header.jsp"></jsp:include>

<!-- TODO: HTML / JS form validation -->
<form action="change-password" method="post">
    <label for="old-password">Old Password:</label>
    <input type="password" name="old-password" id="old-password" required><br>
    <hr>
    <label for="new-password">New Password:</label>
    <input type="password" name="new-password" id="new-password" required><br>
    <label for="repeat-new-password">Repeat New Password:</label>
    <input type="password" name="repeat-new-password" id="repeat-new-password" required><br>
    <input type="hidden" name="id" value="${user.id}"><br>
    <input type="submit" value="change password">
</form>

<hr>

<button onclick="history.go(-1)">back</button>

</body>
</html>

