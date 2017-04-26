<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<!-- TODO: HTML / JS form validation -->
<form action="login" method="post">
    <label for="email">Email address:</label>
    <input type="email" name="email" id="email" required><br>
    <label for="password">Password:</label>
    <input type="password" name="password" id="password" required><br>
    <input type="submit" value="login">
</form>

<c:if test="${error.equals('email')}">
<p>
    Incorrect email address<br>
    <a href="add-new-user-account">Need to create a new account?</a>
</p>
</c:if>
<c:if test="${error.equals('password')}">
    <p>
        Incorrect password<br>
        If the problem persists, contact a system administrator.
    </p>
</c:if>

</body>
</html>
