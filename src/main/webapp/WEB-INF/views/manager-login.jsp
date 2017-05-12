<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<div class="row">
    <div class="col-sm-10 col-sm-offset-1 hid">
        <div class="col-sm-6 col-sm-offset-3">
            <form class="form-vertical" action="login" method="post">

                <div class="spacer_2em"></div>

                <div class="form-group">
                    <label for="email">Email address:</label>
                    <input type="email" class="form-control" name="email" id="email" required>
                </div>

                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" class="form-control" name="password" id="password" required>
                </div>

                <div class="spacer_1em"></div>

                <div class="form-group">
                    <div class="col-md-12 text-center">
                        <input type="submit" class="btn btn-primary" value="Log in to the Console">
                    </div>
                </div>

            </form>

            <c:if test="${error.equals('email')}">
                <div class="row">
                    <div class="col-md-12 text-center">
                        <a href="add-new-user-account">Need to create a new account?</a>
                    </div>
                </div>
            </c:if>

            <c:if test="${error.equals('password')}">
                <div class="row">
                    <div class="col-md-12 text-center">
                        <a href="add-new-user-account">Need to create a new account?</a>
                    </div>
                </div>
            </c:if>

        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>

</body>
</html>
