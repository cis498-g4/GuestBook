<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<!-- TODO: HTML / JS form validation -->
<form class="form-horizontal" action="add-user" method="post">
    <div class="form-group">
        <label class="control-label col-sm-2" for="first-name">First Name:</label>
        <div class="col-sm-4">
            <input type="text" class="form-control" name="first-name" id="first-name" required>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-2" for="last-name">Last Name:</label>
        <div class="col-sm-4">
            <input type="text" class="form-control" name="last-name" id="last-name" required>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-2" for="email">Email Address:</label>
        <div class="col-sm-4">
            <input type="email" class="form-control" name="email" id="email" required>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-2" for="type">User type:</label>
        <div class="col-sm-2">
            <select class="form-control" name="type" id="type" selected="GUEST">
                <option value="GUEST">Guest</option>
                <option value="ORGANIZER">Organizer</option>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-2" for="password">Password</label>
        <div class="col-sm-4">
            <input type="password" class="form-control" name="password" id="password" required>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-2" for="pwd-conf">Retype Password</label>
        <div class="col-sm-4">
            <input type="password" class="form-control" name="pwd-conf" id="pwd-conf" required>
        </div>
    </div>

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <input type="submit" value="Create User" class="btn btn-success">
        </div>
    </div>

</form>

<hr>

<button class="btn btn-primary" onclick="history.go(-1)">Back</button>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>
