<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<!-- TODO: HTML / JS form validation -->
<form class="form-horizontal" action="update-user" method="post">
    <div class="form-group">
        <label class="control-label col-sm-2" for="first-name">First Name:</label>
        <div class="col-sm-4">
            <input type="text" class="form-control" name="first-name" id="first-name" value="${user.firstName}" required>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-2" for="last-name">Last Name:</label>
        <div class="col-sm-4">
            <input type="text" class="form-control" name="last-name" id="last-name" value="${user.lastName}" required>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-2" for="email">Email Address:</label>
        <div class="col-sm-4">
            <input type="email" class="form-control" name="email" id="email" value="${user.email}" required>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-2" for="type">User type:</label>
        <div class="col-sm-2">
            <select class="form-control" name="type" id="type" selected="GUEST">
                <option value="GUEST" ${user.type == "GUEST" ? "selected" : ""}>Guest</option>
                <option value="ORGANIZER" ${user.type == "ORGANIZER" ? "selected" : ""}>Organizer</option>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-2" for="type">Password:</label>
        <div class="col-sm-4">
            <a class="btn btn-default btn-block" href="update-password?id=${user.id}">Click here to update</a>
        </div>
    </div>

    <div class="form-group">
        <input type="hidden" name="id" value="${user.id}">
        <div class="col-sm-offset-2 col-sm-10">
            <input type="submit" value="Update User Information" class="btn btn-success">
        </div>
    </div>

</form>

<hr>

<button class="btn btn-primary" onclick="history.go(-1)">back</button>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>
