<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<% response.setHeader("Refresh", "10;url=" + request.getServletContext().getContextPath() + "/kiosk"); %>

<c:if test="${status <= 3}">
    <img src="${pageContext.request.contextPath}/img/cool_cat.png" width="100">
</c:if>

<c:if test="${status == 1}">
    <h3>Don't forget to complete the survey!</h3>
</c:if>

<p>
    ${message1}
    <br>
    ${message2}
</p>

<a href="${pageContext.request.contextPath}/kiosk">Return to Sign-in</a>

<c:if test="${status == 4}">
    <a href="${pageContext.request.contextPath}/manager/add-new-user-account?ref=kiosk">Create new user</a>
</c:if>

</body>
</html>
