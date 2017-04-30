<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<c:if test="${remain >= 0}">
    <h4>${remain} seats remaining</h4>
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
        <c:if test="${attendanceList.isEmpty()}">
            <tr>
                <td colspan="4" align="center">No registrations found</td>
            </tr>
        </c:if>

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

</div><!--container-->

<script>

    $(document).ready(function() {
        var table = $('#user-regs-list').DataTable( {
            dom: '<"row"<"col-md-12"i>>' +
            '<"row"<"col-md-6"l><"col-md-6"f>>' +
            '<"row"<"col-md-12"rt>>' +
            '<"spacer">' +
            '<"row"<"col-md-6"B><"col-md-6"p>>',
            columnDefs: [ { orderable: false, targets: [2] } ],
            buttons: [
                { extend: 'csv', text: 'Download CSV', className: 'btn-primary' },
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
