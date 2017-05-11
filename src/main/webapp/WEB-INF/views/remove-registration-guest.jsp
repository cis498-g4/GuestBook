<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<div class="spacer_1em"></div>

<p class="text-center">This will <strong>remove</strong> your registration from the event ${event.name} on ${eventLongDate}.</p>
<p class="text-center">${warningMessage}</p>

<form action="remove-registration-guest" method="post">
    <div class="form-group text-center">
        <input type="hidden" name="eventId" value="${event.id}">
        <label>Are you sure?</label>
    </div>

    <div class="form-group text-center">
        <a class="btn btn-primary col" href="${back}">Cancel</a>
        <input type="submit" class="btn btn-danger" value="Remove Registration">
    </div>

</form>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>

</body>
</html>
