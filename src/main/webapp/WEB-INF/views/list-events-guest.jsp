<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<table class="table table-responsive" id="guest-events-list">
    <thead>
        <tr>
            <th>Event Name</th>
            <th>Start</th>
            <th>Presenter</th>
            <th>Status</th>
            <th></th>
        </tr>
    </thead>

    <tbody>
        <c:if test="${userAttendance.isEmpty()}">
            <tr>
                <td colspan="5" align="center">No events found</td>
            </tr>
        </c:if>

        <c:forEach items="${userAttendance}" var="attendance">
            <tr>
                <td>${attendance.event.name}</td>
                <td>
                        ${attendance.event.startDateTime.getMonthValue()}/${attendance.event.startDateTime.getDayOfMonth()}/${attendance.event.startDateTime.getYear()}
                        ${attendance.event.startDateTime.getHour()}:${attendance.event.startDateTime.getMinute() < 10 ? "0" : ""}${attendance.event.startDateTime.getMinute()}
                </td>
                <td>${attendance.event.presenter.lastName}, ${attendance.event.presenter.firstName}</td>
                <td>
                    <c:if test="${attendance.status == 'NOT_ATTENDED'}">Not attended</c:if>
                    <c:if test="${attendance.status == 'SIGNED_IN'}">Signed in</c:if>
                    <c:if test="${attendance.status == 'ATTENDED'}">Attended</c:if>
                </td>
                <td>
                    <form action="show-event-guest">
                        <input type="hidden" name="eventId" value="${attendance.event.id}">
                        <input type="submit" class="btn btn-link btn-block" value="view details">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>

</table>

</div><!--container-->

<script>

    $(document).ready(function() {
        var table = $('#guest-events-list').DataTable( {
            dom: '<"row"<"col-sm-12"i>>' +
            '<"row"<"col-sm-6"l><"col-sm-6"f>>' +
            '<"row"<"col-sm-12"rt>>' +
            '<"dt_spacer_20">' +
            '<"row"<"col-sm-6"B><"col-sm-6"p>>',
            columnDefs: [ { orderable: false, targets: [4] } ],
            buttons: [
                { extend: 'csv', text: 'Download CSV', className: 'btn-primary' },
                { extend: 'print', className: 'btn-primary'}
            ]
        });
    });

</script>

</body>
</html>
