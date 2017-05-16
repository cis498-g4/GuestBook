<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<div class="text-center">
    <i class="fa fa-exclamation-triangle fa-5x" aria-hidden="true"></i>

    <div class="spacer_1em"></div>

    <p>
        ${message}<br>
        Thank you for your interest in the event. We hope to see you again in the future.
    </p>

</div>

<jsp:include page="/WEB-INF/templates/footer-kiosk.jsp"></jsp:include>

</body>
</html>
