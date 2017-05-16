<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<table class="table table-responsive" id="events-list">
    <thead>
        <tr>
            <th>Event Name</th>
            <th>Start</th>
            <th>End</th>
            <th>Presenter</th>
            <th>Reg. Type</th>
            <th>Registration Code</th>
            <th>Survey Required</th>
            <th>Max Capacity</th>
            <th></th>
            <th></th>
            <th></th>
        </tr>
    </thead>

    <tbody>
        <c:forEach items="${events}" var="event">
            <tr>
                <td>${event.name}</td>
                <td>${event.startDateTime.getMonthValue()}/${event.startDateTime.getDayOfMonth()}/${event.startDateTime.getYear()} ${event.startDateTime.getHour()}:${event.startDateTime.getMinute() < 10 ? "0" : ""}${event.startDateTime.getMinute()}</td>
                <td>${event.endDateTime.getMonthValue()}/${event.endDateTime.getDayOfMonth()}/${event.endDateTime.getYear()} ${event.endDateTime.getHour()}:${event.endDateTime.getMinute() < 10 ? "0" : ""}${event.endDateTime.getMinute()}</td>
                <td>${event.presenter.lastName}, ${event.presenter.firstName}</td>
                <td>${event.openRegistration ? "Open" : "Closed"}</td>
                <td>${event.registrationCode != null ? event.registrationCode : "<em>none</em>"}</td>
                <td>${event.mandatorySurvey ? "Yes" : "No"}</td>
                <td>${event.capacity > 0 ? event.capacity : "<em>none</em>"}</td>

                <td>
                    <form action="show-event-info">
                        <input type="hidden" name="id" value="${event.id}">
                        <input type="submit" class="btn btn-link btn-block" value="info">
                    </form>
                </td>
                <td>
                    <form action="update-event">
                        <input type="hidden" name="id" value="${event.id}">
                        <input type="submit" class="btn btn-link btn-block" value="update">
                    </form>
                </td>
                <td>
                    <form action="remove-event">
                        <input type="hidden" name="id" value="${event.id}">
                        <input type="submit" class="btn btn-link btn-block" value="remove">
                    </form>
                </td>

            </tr>
        </c:forEach>
    </tbody>

</table>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>

<script>
    // jQuery DataTables https://datatables.net/
    $(document).ready(function() {
        var table = $('#events-list').DataTable( {
            dom: '<"row"<"col-sm-12"i>>' +
            '<"row"<"col-sm-6"l><"col-sm-6"f>>' +
            '<"row"<"col-sm-12"rt>>' +
            '<"spacer_20">' +
            '<"row"<"col-sm-6"B><"col-sm-6"p>>',
            columnDefs: [ { orderable: false, targets: [8, 9, 10] } ],
            buttons: [
                { extend: 'csv', text: 'Download CSV', className: 'btn-primary' },
                { extend: 'print', className: 'btn-primary'},
                {
                    text: '+ Add new event',
                    className: 'btn-success',
                    action: function ( e, dt, node, conf ) {
                        window.location.href = 'add-event';
                    }
                }
            ]
        });
    });

</script>

</body>
</html>

