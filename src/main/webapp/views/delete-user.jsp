<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/templates/header.jsp"></jsp:include>

<p>The following user will be <strong>permanently</strong> removed:</p>

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

<form action="delete-user" method="post">
    <p>
        This user will no longer be able to sign in or register for events. Are you sure?<br>
        <input type="hidden" name="id" value="${user.id}"><br>
        <input type="submit" value="confirm delete">
        <button onclick="history.go(-1)">cancel</button>
    </p>
</form>

<hr>

<button onclick="history.go(-1)">back</button>

</body>
</html>


