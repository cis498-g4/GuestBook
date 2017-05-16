<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<p class="text-center">${message}</p>

<div class="spacer_1em"></div>

<div class="text-center">
    <a class="btn btn-primary" href="${back}">Back</a>
</div>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>

</body>

</html>

