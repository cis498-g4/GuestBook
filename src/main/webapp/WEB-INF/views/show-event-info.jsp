<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

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

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>

<script>
    // jQuery DataTables https://datatables.net/
    $(document).ready(function() {
        var table = $('#event-info').DataTable( {
            dom: '<"row"<"col-sm-4 col-sm-offset-4"rt>>' +
            '<"spacer_2em">' +
            '<"row"<"col-sm-12 text-center"B>>',
            columnDefs: [ { orderable: false, targets: [0,1] } ],
            ordering: false,
            buttons: [
                {
                    text: 'Back',
                    className: 'btn-primary',
                    action: function ( e, dt, node, conf ) {
                        window.location.href = '${back}';
                    }
                },
                {
                    text: 'Update Information',
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
