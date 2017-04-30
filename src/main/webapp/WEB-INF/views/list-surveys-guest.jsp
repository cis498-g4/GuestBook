<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<table class="table table-responsive" id="guest-surveys-list">
    <thead>
        <tr>
            <th>Event Name</th>
            <th>Date</th>
            <th>Presenter</th>
            <th>Survey Required</th>
            <th></th>
        </tr>
    </thead>

    <tbody>
        <c:if test="${events.isEmpty()}">
            <tr>
                <td colspan="5" align="center">No pending surveys found</td>
            </tr>
        </c:if>

        <c:forEach items="${events}" var="event">
            <tr>
                <td>${event.name}</td>
                <td>${event.startDateTime.getMonthValue()}/${event.startDateTime.getDayOfMonth()}/${event.startDateTime.getYear()}</td>
                <td>${event.presenter.lastName}, ${event.presenter.firstName}</td>
                <td>${event.mandatorySurvey ? "<strong>Yes</strong>" : "No"}</td>
                <td>
                    <form action="take-survey">
                        <input type="hidden" name="eventId" value="${event.id}">
                        <input type="submit" class="btn btn-link btn-block" value="take survey">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>

</table>

</div><!--container-->

<script>

    $(document).ready(function() {
        var table = $('#guest-surveys-list').DataTable( {
            dom: '<"row"<"col-sm-12"i>>' +
            '<"row"<"col-sm-6"l><"col-sm-6"f>>' +
            '<"row"<"col-sm-12"rt>>' +
            '<"spacer">' +
            '<"row"<"col-sm-12"p>>',
            columnDefs: [ { orderable: false, targets: [4] } ]
        });
    });

</script>

</body>
</html>