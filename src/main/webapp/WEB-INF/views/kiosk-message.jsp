<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<% response.setHeader("Refresh", "5;url=/kiosk"); %>

<c:if test="${status == 1}">
    <h2>Don't forget to complete the survey!</h2>
</c:if>

<p>
    ${message1}
    <br>
    ${message2}
</p>

<a href="/kiosk">Return to Sign-in</a>

<c:if test="${status == 4}">
    <a href="/add-user-kiosk">Create new user</a>
</c:if>

</body>
</html>
