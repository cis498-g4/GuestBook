<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<p>The following user will be <strong>permanently</strong> deleted from the database:</p>

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

<p>This user will no longer be able to ${user.type == 'ORGANIZER' ? 'organize' : 'sign in or register for'} events. Are you sure?</p>

<form action="remove-user" method="post">
    <input type="hidden" name="id" value="${user.id}"><br>
    <table>
        <tr>
            <td><input type="submit" value="confirm delete"></td>
            <td><button onclick="history.go(-1)">cancel</button></td>
        </tr>
    </table>

</form>

<hr>

<button onclick="history.go(-1)">back</button>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>


