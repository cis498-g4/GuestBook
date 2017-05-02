<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<div class="spacer_1em"></div>

<p class="text-center">The following user will be <strong>permanently</strong> deleted from the database:</p>

<div class="row">
    <div class="col-sm-4 col-sm-offset-4">
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
    </div>
</div>

<form class="form form-vertical" action="remove-user" method="post">
    <div class="form-group text-center">
        <label>This user will no longer be able to ${user.type == 'ORGANIZER' ? 'organize' : 'sign in or register for'} events. Are you sure?</label>
        <input type="hidden" name="id" value="${user.id}">
    </div>
    <div class="form-group text-center">
        <a class="btn btn-primary" href="javascript:history.go(-1)">Cancel</a>
        <input type="submit" class="btn btn-danger col" value="Confirm Delete">
    </div>

</form>

</div><!--container-->

<script>

    $(document).ready(function() {
        var table = $('#user-info').DataTable( {
            dom: '<"row"<"col-sm-12"rt>>' +
            '<"spacer_20">',
            columnDefs: [ { orderable: false, targets: [0,1] } ]
        });
    });

</script>

</body>
</html>
