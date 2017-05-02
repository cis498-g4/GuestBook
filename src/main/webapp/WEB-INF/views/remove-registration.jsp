<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<div class="spacer_1em"></div>

<p class="text-center">This will <strong>remove</strong> the registration for ${user.firstName} ${user.lastName} from the event ${event.name} on ${eventLongDate}.</p>
<c:if test="${event.capacity > 0}">
    <p class="text-center">After removing the registration, there will be <strong>${event.capacity - event.numRegistered + 1}</strong> open seats at this event.</p>
</c:if>

<form action="remove-registration" method="post">
    <div class="form-group text-center">
        <input type="hidden" name="userId" value="${user.id}">
        <input type="hidden" name="eventId" value="${event.id}">
        <label>Are you sure?</label>
    </div>

    <div class="form-group text-center">
        <a class="btn btn-primary col" href="javascript:history.go(-1)">Cancel</a>
        <input type="submit" class="btn btn-danger col" value="Confirm Delete">
    </div>

</form>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>


