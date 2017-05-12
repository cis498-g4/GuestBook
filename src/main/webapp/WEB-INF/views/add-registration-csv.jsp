<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<table class="table table-responsive" id="csv-regs-list">
    <thead>
        <tr>
            <th>User Last Name</th>
            <th>User First Name</th>
            <th>User Email</th>
            <th>Status</th>
        </tr>
    </thead>

    <tbody>
        <c:forEach items="${existingUsers}" var="existingUser">
            <tr>
                <td>${existingUser.lastName}</td>
                <td>${existingUser.firstName}</td>
                <td>${existingUser.email}</td>
                <td class="text-center"><span class="hidden">Registered</span></td>
            </tr>
        </c:forEach>

        <c:forEach items="${newUsers}" var="newUser">
            <tr>
                <td>${newUser.lastName}</td>
                <td>${newUser.firstName}</td>
                <td>${newUser.email}</td>
                <td class="text-center"><span class="label label-success">New User</span></td>
            </tr>
        </c:forEach>

        <c:forEach items="${errorUsers}" var="errorUser">
            <tr>
                <td>${errorUser.lastName}</td>
                <td>${errorUser.firstName}</td>
                <td>${errorUser.email}</td>
                <td class="text-center"><span class="label label-danger">Failed</span></td>
            </tr>
        </c:forEach>

    </tbody>

</table>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>

<script>

    $(document).ready(function() {
        var table = $('#csv-regs-list').DataTable( {
            dom: '<"row"<"col-sm-12"i>>' +
            '<"row"<"col-sm-6"l><"col-sm-6"f>>' +
            '<"row"<"col-sm-12"rt>>' +
            '<"spacer_20">' +
            '<"row"<"col-sm-6"B><"col-sm-6"p>>',
            columnDefs: [ {
                targets: [0, 1, 2],
                render: $.fn.dataTable.render.ellipsis(25)
            } ],
            order: [[3, 'desc']],
            pageLength: 100,
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
                    text: 'Register more',
                    className: 'btn-success',
                    action: function ( e, dt, node, conf ) {
                        $.redirect('add-registration', { id: 13 })
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

    // render ellipses for data longer than maxLen
    $.fn.dataTable.render.ellipsis = function (maxLen) {
        return function (data, type, row) {
            return type === 'display' && data.length > maxLen ?
                data.substr(0, maxLen) +'&hellip;' :
                data;
        }
    };

</script>

</body>
</html>
