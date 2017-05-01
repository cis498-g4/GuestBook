<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<p>The following user will be <strong>permanently</strong> deleted from the database:</p>

<table class="table table-condensed info-list" id="user-info">
    <thead hidden></thead>
    <tbody>
        <tr>
            <td class="col-xs-3"><strong>First Name:</strong></td>
            <td>${user.firstName}</td>
        </tr>
        <tr>
            <td class="col-xs-3"><strong>Last Name:</strong></td>
            <td>${user.lastName}</td>
        </tr>
        <tr>
            <td class="col-xs-3"><strong>Email Address:</strong></td>
            <td>${user.email}</td>
        </tr>
        <tr>
            <td class="col-xs-3"><strong>User Type:</strong></td>
            <td>${user.type == 'ORGANIZER' ? 'Organizer' : 'Guest'}</td>
        </tr>
    </tbody>
</table>

<form class="form form-vertical" action="remove-user" method="post">
    <div class="form-group">
        <label>This user will no longer be able to ${user.type == 'ORGANIZER' ? 'organize' : 'sign in or register for'} events. Are you sure?</label>
        <input type="hidden" name="id" value="${user.id}">
    </div>
    <div class="form-group">
        <button class="btn btn-primary col" onclick="history.go(-1)">Cancel</button>
        <input type="submit" class="btn btn-danger col" value="Confirm Delete">
    </div>

</form>

</div><!--container-->

<script>

    $(document).ready(function() {
        var table = $('#user-info').DataTable( {
            dom: '<"row"<"col-sm-12"rt>>' +
            '<"dt_spacer_20">',
            columnDefs: [ { orderable: false, targets: [0,1] } ]
        });
    });

</script>

</body>
</html>
