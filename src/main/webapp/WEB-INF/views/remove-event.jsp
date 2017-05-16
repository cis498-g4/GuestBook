<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<p class="text-center">The following event will be <strong>permanently</strong> deleted from the database:</p>

<div class="row">
    <div class="col-sm-4 col-sm-offset-4">
        <table class="table table-condensed info-list" id="event-info">
            <thead hidden></thead>
            <tbody>
                <tr>
                    <td class="col-xs-6"><strong>Event Name:</strong></td>
                    <td class="col-xs-6">${event.name}</td>
                </tr>
                <tr>
                    <td class="col-xs-6"><strong>Event Start:</strong></td>
                    <td class="col-xs-6">
                        ${event.startDateTime.getMonthValue()}/${event.startDateTime.getDayOfMonth()}/${event.startDateTime.getYear()}
                        ${event.startDateTime.getHour()}:${event.startDateTime.getMinute() < 10 ? "0" : ""}${event.startDateTime.getMinute()}
                    </td>
                </tr>
                <tr>
                    <td class="col-xs-6"><strong>Event End:</strong></td>
                    <td class="col-xs-6">
                        ${event.endDateTime.getMonthValue()}/${event.endDateTime.getDayOfMonth()}/${event.endDateTime.getYear()}
                        ${event.endDateTime.getHour()}:${event.endDateTime.getMinute() < 10 ? "0" : ""}${event.endDateTime.getMinute()}
                    </td>
                </tr>
                <tr>
                    <td class="col-xs-6"><strong>Presenter:</strong></td>
                    <td class="col-xs-6">${event.presenter.firstName} ${event.presenter.lastName}</td>
                </tr>
                <tr>
                    <td class="col-xs-6"><strong>Registration Type:</strong></td>
                    <td class="col-xs-6">${event.openRegistration ? "Open" : "Closed"}</td>
                </tr>
                <tr>
                    <td class="col-xs-6"><strong>Registration Code:</strong></td>
                    <td class="col-xs-6">${event.registrationCode != null ? event.registrationCode : "<em>none</em>"}</td>
                </tr>
                <tr>
                    <td class="col-xs-6"><strong>Survey Required:</strong></td>
                    <td class="col-xs-6">${event.mandatorySurvey ? "Yes" : "No"}</td>
                </tr>
                <tr>
                    <td class="col-xs-6"><strong>Max Capacity:</strong></td>
                    <td class="col-xs-6">${event.capacity > 0 ? event.capacity : "<em>none</em>"}</td>
                </tr>
            </tbody>
        </table>
    </div>
</div>

<form class="form form-vertical" action="remove-event" method="post">
    <div class="form-group text-center">
        <label>This effectively cancels the event. Are you sure?</label>
        <input type="hidden" name="id" value="${event.id}">
    </div>
    <div class="form-group text-center">
        <a class="btn btn-primary" href="${back}">Cancel</a>
        <input type="submit" class="btn btn-danger col" value="Confirm Delete">
    </div>
</form>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>

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
