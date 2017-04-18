<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<table>
    <tr>
        <td><strong>First Name:</strong></td>
        <td>${user.firstName}</td>
    </tr>
    <tr>
        <td><strong>Last Name:</strong></td>
        <td>${user.lastName}</td>
    </tr>
    <tr>
        <td><strong>Email Address:</strong></td>
        <td>${user.email}</td>
    </tr>
    <tr>
        <td><strong>User Type:</strong></td>
        <td>${user.type == 'ORGANIZER' ? 'Organizer' : 'Guest'}</td>
    </tr>
</table>

<hr>

<table>
    <tr>
        <td><button onclick="history.go(-1)">back</button></td>

        <c:forTokens items="edit,delete" delims="," var="action">
        <td>
            <form action="${action}-user">
                <input type="hidden" name="id" value="${user.id}">
                <input type="submit" value="${action}">
            </form>
        </td>
        </c:forTokens>

    </tr>
</table>

</body>
</html>
