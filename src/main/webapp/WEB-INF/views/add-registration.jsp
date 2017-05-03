<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<div class="spacer_1em"></div>

<div class="text-center">
    <h3>Single Registration</h3>
    <p>Register an individual user for ${event.name} by entering their email address below.</p>
</div>

<!-- TODO: HTML / JS form validation -->
<div class="row padding-horiz-10px">
    <div class="col-sm-6 col-sm-offset-3">
        <form class="form-vertical" action="add-registration" method="post">

            <div class="form-group text-center">
                <input type="email" class="form-control" name="email" id="email" placeholder="Email address" required>
            </div>

            <div class="form-group text-center">
                <input type="hidden" name="eventId" value="${event.id}">
                <input type="submit" class="btn btn-success" value="Register ${statusType.equals('success') ? 'Another' : ''} User">
            </div>

        </form>
    </div>
</div>

<hr>

<div class="text-center">
    <h3>Group Registration</h3>
    <p>Register multiple users simultaneously by uploading a CSV in the format last name, first name, email address</p>
</div>

<div class="row padding-horiz-10px">
    <div class="col-sm-6 col-sm-offset-3">
        <form class="form-vertical" action="add-registration-csv" method="post" enctype="multipart/form-data">
            <div class="form-group text-center">
                <div class="input-group">
                    <label class="input-group-btn">
                        <span class="btn btn-primary">
                            Choose File <input type="file" name="csv-list" style="display: none;" required>
                        </span>
                    </label>
                    <input type="text" class="form-control" readonly>
                </div>
            </div>

            <div class="form-group text-center">
                <input type="hidden" name="eventId" value="${event.id}">
                <input type="submit" class="btn btn-success" value="Submit Registrations">
            </div>

        </form>
    </div>
</div>

<hr>

<div class="text-center">
    <a class="btn btn-primary" href="list-user-regs-for-event?id=${event.id}">Back to Registration List</a>
</div>

</div><!--container-->

<script>
/**
 * Source:
 * https://www.abeautifulsite.net/whipping-file-inputs-into-shape-with-bootstrap-3
 */
    // Attach the `fileselect` event to all file inputs on the page
    $(document).on('change', ':file', function() {
        var input = $(this),
            numFiles = 1,
            label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
        input.trigger('fileselect', [numFiles, label]);
    });

    // We can watch for our custom `fileselect` event like this
    $(document).ready( function() {
        $(':file').on('fileselect', function(event, numFiles, label) {

            var input = $(this).parents('.input-group').find(':text'),
                log = label;

            if( input.length ) {
                input.val(log);
            } else {
                if( log ) alert(log);
            }

        });
    });

</script>

</body>
</html>
