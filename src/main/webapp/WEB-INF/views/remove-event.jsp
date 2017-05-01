<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<p>The following event will be <strong>permanently</strong> deleted from the database:</p>

<table class="table table-condensed info-list" id="event-info">
    <thead hidden></thead>
    <tbody>
        <tr>
            <td class="col-xs-3"><strong>Event Name:</strong></td>
            <td>${event.name}</td>
        </tr>
        <tr>
            <td class="col-xs-3"><strong>Event Start:</strong></td>
            <td>
                ${event.startDateTime.getMonthValue()}/${event.startDateTime.getDayOfMonth()}/${event.startDateTime.getYear()}
                ${event.startDateTime.getHour()}:${event.startDateTime.getMinute() < 10 ? "0" : ""}${event.startDateTime.getMinute()}
            </td>
        </tr>
        <tr>
            <td class="col-xs-3"><strong>Event End:</strong></td>
            <td>
                ${event.endDateTime.getMonthValue()}/${event.endDateTime.getDayOfMonth()}/${event.endDateTime.getYear()}
                ${event.endDateTime.getHour()}:${event.endDateTime.getMinute() < 10 ? "0" : ""}${event.endDateTime.getMinute()}
            </td>
        </tr>
        <tr>
            <td class="col-xs-3"><strong>Presenter:</strong></td>
            <td>${event.presenter.firstName} ${event.presenter.lastName}</td>
        </tr>
        <tr>
            <td class="col-xs-3"><strong>Registration Type:</strong></td>
            <td>${event.openRegistration ? "Open" : "Closed"}</td>
        </tr>
        <tr>
            <td class="col-xs-3"><strong>Registration Code:</strong></td>
            <td>${event.registrationCode != null ? event.registrationCode : "<em>none</em>"}</td>
        </tr>
        <tr>
            <td class="col-xs-3"><strong>Survey Required:</strong></td>
            <td>${event.mandatorySurvey ? "Yes" : "No"}</td>
        </tr>
        <tr>
            <td class="col-xs-3"><strong>Max Capacity:</strong></td>
            <td>${event.capacity > 0 ? event.capacity : "<em>none</em>"}</td>
        </tr>
    </tbody>
</table>

<form class="form form-vertical" action="remove-event" method="post">
    <div class="form-group">
        <label>This effectively cancels the event. Are you sure?</label>
        <input type="hidden" name="id" value="${event.id}">
    </div>
    <div class="form-group">
        <button class="btn btn-primary col" onclick="history.go(-1)">Cancel</button>
        <input type="submit" class="btn btn-danger col" value="Confirm Delete">
    </div>
</form>

</div><!--container-->

<script>

    $(document).ready(function() {
        var table = $('#event-info').DataTable( {
            dom: '<"row"<"col-sm-12"rt>>' +
            '<"spacer_20">',
            columnDefs: [ { orderable: false, targets: [0,1] } ]
        });
    });

</script>

</body>
</html>
