<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/templates/header.jsp"></jsp:include>

<p>The following user will be <strong>permanently</strong> removed:</p>

<table>
    <tr>
        <th>First Name:</th>
        <td>${user.firstName}</td>
    </tr>
    <tr>
        <th>Last Name:</th>
        <td>${user.lastName}</td>
    </tr>
    <tr>
        <th>Email Address:</th>
        <td>${user.email}</td>
    </tr>
    <tr>
        <th>User Type:</th>
        <td>${user.type == 'ORGANIZER' ? 'Organizer' : 'Guest'}</td>
    </tr>
</table>

<form action="delete-user" method="post">
    <p>
        You sure 'bout that?<br>
        <input type="hidden" name="id" value="${user.id}"><br>
        <input type="submit" value="confirm delete">
        <button onclick="history.go(-1)">cancel</button>
    </p>
</form>

<hr>

<button onclick="history.go(-1)">back</button>

</body>
</html>


