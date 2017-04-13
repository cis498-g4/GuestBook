<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <title>List</title>
</head>
<body>

<h2>Users</h2>

<table>
    <tr>
        <th>Last Name</th>
        <th>First Name</th>
        <th>Email</th>
        <th>Type</th>
    </tr>
    <c:forEach items="${users}" var="user">
    <tr>
        <td>${user.lastName}</td>
        <td>${user.firstName}</td>
        <td>${user.email}</td>
        <td>${user.type == 'ORGANIZER' ? 'Organizer' : 'Guest'}</td>
        <c:forTokens items="view,edit,delete" delims="," var="action">
        <td>
            <form action="/manager/${action}-user">
                <input type="hidden" name="userId" value="${user.id}">
                <input type="submit" value="${action}">
            </form>
        </td>
        </c:forTokens>
    </tr>
    </c:forEach>
</table>

</body>
</html>
