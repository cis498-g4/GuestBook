<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <title>${pageTitle}</title>
</head>
<body>

<div>${statusMessage}</div>

<h2>${pageTitle}</h2>

<table>
    <tr>

        <c:forTokens items="Last Name,First Name,Email Address,User Type" delims="," var="col_head">
        <th>${col_head}</th>
        </c:forTokens>

    </tr>

    <c:forEach items="${users}" var="user">
    <tr>
        <td>${user.lastName}</td>
        <td>${user.firstName}</td>
        <td>${user.email}</td>
        <td>${user.type == 'ORGANIZER' ? 'Organizer' : 'Guest'}</td>

        <c:forTokens items="view,edit,delete" delims="," var="action">
        <td>
            <form action="${action}-user">
                <input type="hidden" name="id" value="${user.id}">
                <input type="submit" value="${action}">
            </form>
        </td>
        </c:forTokens>

    </tr>
    </c:forEach>

</table>

</body>
</html>
