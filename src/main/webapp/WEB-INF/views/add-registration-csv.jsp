<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<div class="spacer_1em"></div>

<h4>Existing Users</h4>

<ul>
    <c:forEach items="${existingUsers}" var="exUser">
        <li>${exUser.firstName} ${exUser.lastName} ${exUser.email}</li>
    </c:forEach>
</ul>

<h4>New Users</h4>

<ul>
    <c:forEach items="${newUsers}" var="newUser">
        <li>${newUser.firstName} ${newUser.lastName} ${newUser.email}</li>
    </c:forEach>
</ul>

<h4>Error Users</h4>

<ul>
    <c:forEach items="${errorUsers}" var="errUser">
        <li>${errUser.firstName} ${errUser.lastName} ${errUser.email}</li>
    </c:forEach>
</ul>


</div><!--container-->

</body>
</html>
