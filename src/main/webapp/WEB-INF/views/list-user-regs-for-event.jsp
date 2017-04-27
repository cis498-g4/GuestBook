<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<c:if test="${remain >= 0}">
    <p><strong>${remain} seats remaining</strong></p>
</c:if>

<table class="table table-responsive">
    <thead>
        <tr>
            <th>User Name</th>
            <th>User Email</th>
            <th></th>
        </tr>
    </thead>

    <tbody>
        <c:if test="${attendanceList.isEmpty()}">
            <tr>
                <td colspan="4" align="center">No registrations found</td>
            </tr>
        </c:if>

        <c:forEach items="${attendanceList}" var="attendance">
            <tr>
                <td>${attendance.user.lastName}, ${attendance.user.firstName}</td>
                <td>${attendance.user.email}</td>
                <td>
                    <form action="remove-registration">
                        <input type="hidden" name="userId" value="${attendance.user.id}">
                        <input type="hidden" name="eventId" value="${event.id}">
                        <input type="submit" class="btn btn-danger btn-sm btn-block" value="remove registration">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>

</table>

<br>


<br>

<hr>

<form action="filter-reg">
    <label for="field">Filter by: </label>
    <select name="field" id="field">
        <option>User Name</option>
        <option>User email</option>
    </select>
    <input type="text" name="value">
    <input type="checkbox" name="exact" checked>Exact matches only
    <input type="submit" class="btn btn-default btn-sm" value="submit">
</form>

<hr>

<table>
    <tr>
        <td><button class="btn btn-default btn-sm btn-block" onclick="history.go(-1)">back</button></td>
        <td>
            <form action="add-registration">
                <input type="hidden" name="id" value="${event.id}">
                <input type="submit" class="btn btn-success btn-sm btn-block" value="register new users">
            </form>
        </td>
    </tr>
</table>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>
