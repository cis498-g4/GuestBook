<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<!-- TODO: HTML / JS form validation -->
<form action="update-password-guest" method="post">
    <label for="old-password">Old Password:</label>
    <input type="password" name="old-password" id="old-password" required>
    <c:if test="${error.equals('oldpass')}">
        Please enter your current password
    </c:if>
    <br>
    <br>
    <label for="new-password">New Password:</label>
    <input type="password" name="new-password" id="new-password" required><br>
    <label for="repeat-password">Repeat New Password:</label>
    <input type="password" name="repeat-password" id="repeat-password" required>
    <c:if test="${error.equals('match')}">
        Password fields must match
    </c:if>
    <br>
    <input type="submit" value="update password">
</form>

<hr>

<button onclick="history.go(-1)">back</button>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>
