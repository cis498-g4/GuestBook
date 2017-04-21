<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<p>Presented by ${event.presenter.firstName} ${event.presenter.lastName}</p>

<!-- TODO: HTML / JS form validation -->
<form action="kiosk" method="post">
    <p>Please sign in with your email address:</p>
    <input type="email" name="email" id="email" required>
    <br>
    <input type="submit" value="sign in">
</form>

</body>
</html>

