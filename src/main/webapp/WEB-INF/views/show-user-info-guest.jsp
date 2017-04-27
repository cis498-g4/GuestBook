<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<!-- TODO: HTML / JS form validation -->
<form action="show-user-info-guest" method="post">
    <table>
        <tr>
            <td><strong>First Name:</strong></td>
            <td><input type="text" name="first-name" id="first-name" value="${sessionUser.firstName}" required></td>
        </tr>
        <tr>
            <td><strong>Last Name:</strong></td>
            <td><input type="text" name="last-name" id="last-name" value="${sessionUser.lastName}" required></td>
        </tr>
        <tr>
            <td><strong>Email Address:</strong></td>
            <td><input type="email" name="email" id="email" value="${sessionUser.email}" required></td>
        </tr>
    </table>

    <br>

    <input type="submit" value="update information">

</form>

<a href="update-password-guest">Click here to change your password</a>

<hr>

<button onclick="history.go(-1)">back</button>

</body>
</html>
