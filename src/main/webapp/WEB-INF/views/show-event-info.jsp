<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

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

</div><!--container-->

<script>

    $(document).ready(function() {
        var table = $('#event-info').DataTable( {
            dom: '<"row"<"col-sm-12"rt>>' +
            '<"dt_spacer_20">' +
            '<"dt_hr_20">' +
            '<"row"<"col-sm-12"B>>',
            columnDefs: [ { orderable: false, targets: [0,1] } ],
            buttons: [
                {
                    text: 'Back',
                    className: 'btn-primary',
                    action: function ( e, dt, node, conf ) {
                        window.location.href = 'list-events';
                    }
                },
                {
                    text: 'Update Event Information',
                    className: 'btn-primary',
                    action: function ( e, dt, node, conf ) {
                        $.redirect('update-event', { id: ${event.id} })
                    }
                },
                {
                    text: 'Remove Event',
                    className: 'btn-primary',
                    action: function ( e, dt, node, conf ) {
                        $.redirect('remove-event', { id: ${event.id} })
                    }
                }
            ]
        });
    });

    // redirect extend function
    $.extend( {
        redirect: function(url, args) {
            var form = $('<form></form>');
            form.attr('method', 'get');
            form.attr('action', url);

            $.each(args, function(key, value) {
                var field = $('<input></input>');
                field.attr('type', 'hidden');
                field.attr('name', key);
                field.attr('value', value);
                form.append(field);
            });

            $(form).appendTo('body').submit();
        }
    });

</script>

</body>
</html>
