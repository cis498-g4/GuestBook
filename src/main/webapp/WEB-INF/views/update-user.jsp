<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<div class="spacer_1em"></div>

<!-- TODO: HTML / JS form validation -->
<form class="form-horizontal" action="update-user" method="post">
    <div class="row padding-horiz-10px">
        <div class="col-sm-offset-1">
            <div class="form-group">
                <label class="control-label col-sm-3" for="first-name">First Name:</label>
                <div class="col-sm-5">
                    <input type="text" class="form-control" name="first-name" id="first-name" value="${user.firstName}" required>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-sm-3" for="last-name">Last Name:</label>
                <div class="col-sm-5">
                    <input type="text" class="form-control" name="last-name" id="last-name" value="${user.lastName}" required>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-sm-3" for="email">Email Address:</label>
                <div class="col-sm-5">
                    <input type="email" class="form-control" name="email" id="email" value="${user.email}" required>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-sm-3" for="type">User type:</label>
                <div class="col-sm-3">
                    <select class="form-control" name="type" id="type" selected="GUEST">
                        <option value="GUEST" ${user.type == "GUEST" ? "selected" : ""}>Guest</option>
                        <option value="ORGANIZER" ${user.type == "ORGANIZER" ? "selected" : ""}>Organizer</option>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-sm-3" for="type">Password:</label>
                <div class="col-sm-5">
                    <a class="btn btn-default btn-block" href="update-password?id=${user.id}">Click here to update</a>
                </div>
            </div>

        </div>
    </div>

    <div class="spacer_1em"></div>

    <div class="form-group text-center">
        <a class="btn btn-primary" href="list-users">Cancel</a>
        <input type="hidden" name="id" value="${user.id}">
        <input type="submit" value="Update User Information" class="btn btn-success">
    </div>

</form>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>
