<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<!-- TODO: HTML / JS form validation -->
<form class="form-horizontal" action="update-password" method="post">
    <div class="form-group">
        <label class="control-label col-sm-2" for="old-password">Old Password:</label>
        <div class="col-sm-4">
            <input type="password" class="form-control" name="old-password" id="old-password" required>
            <c:if test="${error.equals('oldpass')}">
                <span class="label label-warning">Please enter the user's current password</span>
            </c:if>
        </div>
    </div>

    <br>

    <div class="form-group">
        <label class="control-label col-sm-2" for="new-password">New Password:</label>
        <div class="col-sm-4">
            <input type="password" class="form-control" name="new-password" id="new-password" required>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-2" for="repeat-password">Retype Password:</label>
        <div class="col-sm-4">
            <input type="password" class="form-control" name="repeat-password" id="repeat-password" required>
        </div>
        <c:if test="${error.equals('match')}">
            <span class="label label-warning">Password fields must match</span>
        </c:if>
    </div>

    <div class="form-group">
        <input type="hidden" name="id" value="${user.id}">
        <div class="col-sm-offset-2 col-sm-10">
            <input type="submit" value="Change Password" class="btn btn-success">
        </div>
    </div>

</form>

<hr>

<button class="btn btn-primary" onclick="history.go(-1)">Back</button>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>
