<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<c:if test="${remain >= 0}">
    <h3 class="text-center">${remain} seats remaining</h3>
</c:if>

<table class="table table-responsive" id="user-regs-list">
    <thead>
        <tr>
            <th>User Name</th>
            <th>User Email</th>
            <th></th>
        </tr>
    </thead>

    <tbody>
        <c:forEach items="${attendanceList}" var="attendance">
            <tr>
                <td>${attendance.user.lastName}, ${attendance.user.firstName}</td>
                <td>${attendance.user.email}</td>
                <td>
                    <form action="remove-registration">
                        <input type="hidden" name="userId" value="${attendance.user.id}">
                        <input type="hidden" name="eventId" value="${event.id}">
                        <input type="submit" class="btn btn-link btn-block" value="remove registration">
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
        var table = $('#user-regs-list').DataTable( {
            dom: '<"row"<"col-sm-12"i>>' +
            '<"row"<"col-sm-6"l><"col-sm-6"f>>' +
            '<"row"<"col-sm-12"rt>>' +
            '<"spacer_20">' +
            '<"row"<"col-sm-6"B><"col-sm-6"p>>',
            columnDefs: [ { orderable: false, targets: [2] } ],
            buttons: [
                {
                    text: 'Back',
                    className: 'btn-primary',
                    action: function (e, dt, node, conf) {
                        window.location.href = '${back}';
                    }
                },
                { extend: 'csv', text: 'CSV', className: 'btn-primary' },
                { extend: 'print', className: 'btn-primary'},
                {
                    text: 'Register new users',
                    className: 'btn-success',
                    action: function ( e, dt, node, conf ) {
                        $.redirect('add-registration', { id: ${event.id} })
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
