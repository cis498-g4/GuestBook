<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<table class="table table-responsive" id="guest-registrations-list">
    <thead>
        <tr>
            <th>Event Name</th>
            <th>Date</th>
            <th>Presenter</th>
            <th>Registration Type</th>
            <th>Registration Code</th>
            <th>Guests Registered</th>
            <th></th>
        </tr>
    </thead>

    <tbody>
        <c:forEach items="${registrations}" var="registration">
            <tr>
                <td>${registration.event.name}</td>
                <td>
                        ${registration.event.startDateTime.getMonthValue()}/${registration.event.startDateTime.getDayOfMonth()}/${registration.event.startDateTime.getYear()}
                        ${registration.event.startDateTime.getHour()}:${registration.event.startDateTime.getMinute() < 10 ? "0" : ""}${registration.event.startDateTime.getMinute()}
                </td>
                <td>${registration.event.presenter.lastName}, ${registration.event.presenter.firstName}</td>
                <td>${registration.event.openRegistration ? "Open" : "Closed"}</td>
                <td>${registration.event.registrationCode != null ? registration.event.registrationCode : "<em>none</em>"}</td>
                <td>
                        ${registration.event.numRegistered} / ${registration.event.capacity > 0 ? registration.event.capacity : "&#8734;"}
                </td>
                <td>
                    <form action="remove-registration-guest">
                        <input type="hidden" name="eventId" value="${registration.event.id}">
                        <input type="submit" class="btn btn-link btn-block" value="Remove Registration">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>

</table>

<hr>

<div class="text-center">
    <h3>Register for a new event</h3>
    <p>If you do not have a registration code, please contact an event organizer to assist you with registration.</p>

    <form class="form-inline" action="add-registration-guest" method="post">
        <div class="input-group">
            <input type="text" class="form-control" name="reg-code" id="reg-code" placeholder="Registration Code" maxlength="8" required>
            <span class="input-group-btn"><input type="submit" class="btn btn-success" value="Register"></span>
        </div>

    </form>

</div>

</div><!--container-->

<script>

    $(document).ready(function() {
        var table = $('#guest-registrations-list').DataTable( {
            dom: '<"row"<"col-sm-12"i>>' +
            '<"row"<"col-sm-6"l><"col-sm-6"f>>' +
            '<"row"<"col-sm-12"rt>>' +
            '<"spacer_20">' +
            '<"row"<"col-sm-6"B><"col-sm-6"p>>',
            columnDefs: [ { orderable: false, targets: [6] } ],
            buttons: [
                { extend: 'csv', text: 'Download CSV', className: 'btn-primary' },
                { extend: 'print', className: 'btn-primary'}
            ]
        });
    });

</script>

</body>
</html>
