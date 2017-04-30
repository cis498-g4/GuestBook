<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<table class="table table-responsive" id="users-list">
    <thead>
        <tr>
            <th>Last Name</th>
            <th>First Name</th>
            <th>Email</th>
            <th>Type</th>
            <th></th>
            <th></th>
            <th></th>
        </tr>
    </thead>

    <tbody>
        <c:if test="${users.isEmpty()}">
            <tr>
                <td colspan="7" align="center">No users found</td>
            </tr>
        </c:if>

        <c:forEach items="${users}" var="user">
            <tr>
                <td>${user.lastName}</td>
                <td>${user.firstName}</td>
                <td>${user.email}</td>
                <td>${user.type == "ORGANIZER" ? "Organizer" : "Guest"}</td>

                <td>
                    <form action="show-user-info">
                        <input type="hidden" name="id" value="${user.id}">
                        <input type="submit" class="btn btn-link btn-block" value="show info">
                    </form>
                </td>
                <td>
                    <form action="update-user">
                        <input type="hidden" name="id" value="${user.id}">
                        <input type="submit" class="btn btn-link btn-block" value="update">
                    </form>
                </td>
                <td>
                    <form action="remove-user">
                        <input type="hidden" name="id" value="${user.id}">
                        <input type="submit" class="btn btn-link btn-block" value="remove">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>

</table>

</div><!--container-->

<script>

    $(document).ready(function() {
        var table = $('#users-list').DataTable( {
            dom: '<"row"<"col-sm-12"i>>' +
            '<"row"<"col-sm-6"l><"col-sm-6"f>>' +
            '<"row"<"col-sm-12"rt>>' +
            '<"dt_spacer_20">' +
            '<"row"<"col-sm-6"B><"col-sm-6"p>>',
            columnDefs: [ { orderable: false, targets: [4, 5, 6] } ],
            buttons: [
                { extend: 'csv', text: 'Download CSV', className: 'btn-primary' },
                { extend: 'print', className: 'btn-primary'},
                {
                    text: '+ Add new user',
                    className: 'btn-success',
                    action: function ( e, dt, node, conf ) {
                        window.location.href = 'add-user';
                    }
                }
            ]
        });
    });

</script>

</body>
</html>
