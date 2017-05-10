<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<div class="spacer_1em"></div>

<form class="form-horizontal" action="add-user" method="post">
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
                <label class="control-label col-sm-3" for="type">User type:</label>
                <div class="col-sm-3">
                    <select class="form-control" name="type" id="type">
                        <option value="GUEST" selected>Guest</option>
                        <option value="ORGANIZER">Organizer</option>
                    </select>
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
        <a class="btn btn-primary" href="${back}">Cancel</a>
        <input type="submit" class="btn btn-success" value="Create User">
    </div>

</form>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>

</body>
</html>
