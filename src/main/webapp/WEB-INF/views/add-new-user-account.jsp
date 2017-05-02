<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<div class="spacer_1em"></div>

<!-- TODO: HTML / JS form validation -->
<form class="form-horizontal" action="add-new-user-account" method="post">
    <div class="row padding-horiz-10px">
        <div class="col-sm-offset-1">
            <div class="form-group">
                <label class="control-label col-sm-3" for="first-name">First Name:</label>
                <div class="col-sm-5">
                    <input type="text" class="form-control" name="first-name" id="first-name" required>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-sm-3" for="last-name">Last Name:</label>
                <div class="col-sm-5">
                    <input type="text" class="form-control" name="last-name" id="last-name" required>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-sm-3" for="email">Email Address:</label>
                <div class="col-sm-5">
                    <input type="email" class="form-control" name="email" id="email" required>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-sm-3" for="password">Password:</label>
                <div class="col-sm-5">
                    <input type="password" class="form-control" name="password" id="password" required>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-sm-3" for="pwd-conf">Retype Password:</label>
                <div class="col-sm-5">
                    <input type="password" class="form-control" name="pwd-conf" id="pwd-conf" required>
                </div>
                <c:if test="${error.equals('match')}">
                    <span class="label label-warning">Password fields must match</span>
                </c:if>
            </div>

        </div>
    </div>

    <div class="spacer_1em"></div>

    <div class="form-group text-center">
        <a class="btn btn-primary" href="${back}">Back to sign-in</a>
        <input type="hidden" name="type" value="GUEST">
        <input type="submit" value="Create User" class="btn btn-success">
    </div>

</form>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>
