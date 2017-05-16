<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<table class="table table-condensed info-list" id="user-info">
    <thead hidden></thead>
    <tbody>
        <tr>
            <td class="col-xs-6"><strong>First Name:</strong></td>
            <td class="col-xs-6">${user.firstName}</td>
        </tr>
        <tr>
            <td class="col-xs-6"><strong>Last Name:</strong></td>
            <td class="col-xs-6">${user.lastName}</td>
        </tr>
        <tr>
            <td class="col-xs-6"><strong>Email Address:</strong></td>
            <td class="col-xs-6">${user.email}</td>
        </tr>
        <tr>
            <td class="col-xs-6"><strong>User Type:</strong></td>
            <td class="col-xs-6">${user.type == 'ORGANIZER' ? 'Organizer' : 'Guest'}</td>
        </tr>
    </tbody>
</table>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>

<script>
    // jQuery DataTables https://datatables.net/
    $(document).ready(function() {
        var table = $('#user-info').DataTable( {
            dom: '<"row"<"col-sm-4 col-sm-offset-4"rt>>' +
            '<"spacer_2em">' +
            '<"row"<"col-sm-12 text-center"B>>',
            columnDefs: [ { orderable: false, targets: [0,1] } ],
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
                        $.redirect('update-user', { id: ${user.id} })
                    }
                },
                {
                    text: 'Remove User',
                    className: 'btn-primary',
                    action: function ( e, dt, node, conf ) {
                        $.redirect('remove-user', { id: ${user.id} })
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
