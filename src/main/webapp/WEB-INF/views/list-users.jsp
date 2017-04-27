<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<table class="table table-responsive">
    <thead>
        <tr>
            <th>Last Name</th>
            <th>First Name</th>
            <th>Email</th>
            <th>Type</th>
            <th></th>
            <th></th>
            <th></th>
        </tr>
    </thead>

    <tbody>
        <c:if test="${users.isEmpty()}">
            <tr>
                <td colspan="7" align="center">No users found</td>
            </tr>
        </c:if>

        <c:forEach items="${users}" var="user">
            <tr>
                <td>${user.lastName}</td>
                <td>${user.firstName}</td>
                <td>${user.email}</td>
                <td>${user.type == "ORGANIZER" ? "Organizer" : "Guest"}</td>

                <td>
                    <form action="show-user-info">
                        <input type="hidden" name="id" value="${user.id}">
                        <input type="submit" class="btn btn-link btn-block" value="show info">
                    </form>
                </td>
                <td>
                    <form action="update-user">
                        <input type="hidden" name="id" value="${user.id}">
                        <input type="submit" class="btn btn-link btn-block" value="update">
                    </form>
                </td>
                <td>
                    <form action="remove-user">
                        <input type="hidden" name="id" value="${user.id}">
                        <input type="submit" class="btn btn-link btn-block" value="remove">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>

</table>

<hr>

<form action="filter-user">
    <label for="field">Filter by: </label>
    <select name="field" id="field">
        <option>Last Name</option>
        <option>First Name</option>
        <option>Email</option>
        <option>Type</option>
    </select>
    <input type="text" name="value">
    <input type="checkbox" name="exact" checked>Exact matches only
    <input type="submit" class="btn btn-default btn-sm" value="submit">
</form>

<hr>

<a class="btn btn-success" href="add-user">+ Add new user</a>

<br>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>
