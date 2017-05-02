<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<h4 class="text-center">Presented by ${event.presenter.firstName} ${event.presenter.lastName}</h4>

<!-- TODO: HTML / JS form validation -->
<div class="row">
    <div class="col-sm-10 col-sm-offset-1 hid">
        <div class="col-sm-6 col-sm-offset-3">
            <form class="form-vertical" action="kiosk" method="post">

                <div class="spacer_2em"></div>

                <div class="form-group text-center">
                    <label for="email">Please sign in with your email address:</label>
                    <input type="email" class="form-control" name="email" id="email" placeholder="yourname@example.com" required>
                </div>

                <div class="form-group text-center">
                    <input type="submit" class="btn btn-primary" value="Sign In">
                </div>

            </form>

        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>
