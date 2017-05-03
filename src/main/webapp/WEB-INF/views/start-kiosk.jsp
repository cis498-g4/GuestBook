<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<div class="spacer_1em"></div>

<p class="text-center">Select an event below to launch the kiosk for that event. This will log you out of your management session and start kiosk mode.</p>

<div class="spacer_1em"></div>

<table class="table table-responsive table-hover" id="events-list">
    <thead>
        <tr>
            <th>Event Name</th>
            <th>Start Date</th>
            <th>Presenter</th>
            <th></th>
        </tr>
    </thead>

    <tbody>
        <c:forEach items="${futureEvents}" var="event">
            <tr>
                <td>${event.name}</td>
                <td>${event.startDateTime.getMonthValue()}/${event.startDateTime.getDayOfMonth()}/${event.startDateTime.getYear()} ${event.startDateTime.getHour()}:${event.startDateTime.getMinute() < 10 ? "0" : ""}${event.startDateTime.getMinute()}</td>
                <td>${event.presenter.lastName}, ${event.presenter.firstName}</td>

                <td>
                    <form action="start-kiosk" method="post">
                        <input type="hidden" name="eventId" value="${event.id}">
                        <input type="submit" class="btn btn-success" value="Launch Kiosk">
                    </form>
                </td>

            </tr>
        </c:forEach>
    </tbody>

</table>

</div><!--container-->

<script>

    $(document).ready(function() {
        var table = $('#events-list').DataTable( {
            dom: '<"row"<"col-sm-12"i>>' +
            '<"row"<"col-sm-6"l><"col-sm-6"f>>' +
            '<"row"<"col-sm-12"rt>>' +
            '<"spacer_20">' +
            '<"row"<"col-sm-6"B><"col-sm-6"p>>',
            columnDefs: [ { orderable: false, targets: [3] } ],
            buttons: [
                {
                    text: 'Back',
                    className: 'btn-primary',
                    action: function ( e, dt, node, conf ) {
                        window.location.href = 'list-events';
                    }
                }
            ]
        });
    });

</script>

</body>
</html>
