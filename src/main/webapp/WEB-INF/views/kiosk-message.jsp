<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<div class="text-center">
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

    <div class="spacer_1em"></div>

    <a class="btn btn-primary" href="${pageContext.request.contextPath}/kiosk">Return to Sign-in</a>

    <c:if test="${status == 4}">
        <a class="btn btn-success" href="kiosk/add-new-user-account">Create new user</a>
    </c:if>
</div>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>

</body>
</html>
