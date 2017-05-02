<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<div class="spacer_1em"></div>

<!-- TODO: HTML / JS form validation -->
<form class="form-horizontal" action="update-password-guest" method="post">
    <div class="row padding-horiz-10px">
        <div class="col-sm-offset-1">
            <div class="form-group">
                <label class="control-label col-sm-3" for="old-password">Old Password:</label>
                <div class="col-sm-5">
                    <input type="password" class="form-control" name="old-password" id="old-password" required>
                    <c:if test="${error.equals('oldpass')}">
                        <span class="label label-warning">Please enter your current password</span>
                    </c:if>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-sm-3" for="new-password">New Password:</label>
                <div class="col-sm-5">
                    <input type="password" class="form-control" name="new-password" id="new-password" required>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-sm-3" for="repeat-password">Repeat New Password:</label>
                <div class="col-sm-5">
                    <input type="password" class="form-control" name="repeat-password" id="repeat-password" required>
                    <c:if test="${error.equals('match')}">
                        <span class="label label-warning">Password fields must match</span>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

    <div class="spacer_1em"></div>

    <div class="form-group text-center">
        <a class="btn btn-primary" href="javascript:history.go(-1)">Cancel</a>
        <input type="submit" class="btn btn-success" value="Change password">
    </div>

</form>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>
