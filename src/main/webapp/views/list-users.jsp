<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/templates/header.jsp"></jsp:include>

<table>
    <tr>
        <th>Last Name</th>
        <th>First Name</th>
        <th>Email</th>
        <th>Type</th>
    </tr>

    <c:if test="${events.isEmpty()}">
        <tr>
            <td colspan="11" align="center">No users found</td>
        </tr>
    </c:if>

    <c:forEach items="${users}" var="user">
    <tr>
        <td>${user.lastName}</td>
        <td>${user.firstName}</td>
        <td>${user.email}</td>
        <td>${user.type == "ORGANIZER" ? "Organizer" : "Guest"}</td>

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
    <input type="submit" value="submit">
</form>

<hr>

<a href="add-user">+ Add new user</a>

</body>
</html>
