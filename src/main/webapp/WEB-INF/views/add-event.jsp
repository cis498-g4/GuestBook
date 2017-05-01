<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<!-- TODO: HTML / JS form validation -->
<form class="form-horizontal" action="add-event" method="post">
    <div class="form-group">
        <label class="control-label col-sm-2" for="name">Event Name:</label>
        <div class="col-sm-5">
            <input type="text" class="form-control" name="name" id="name" required>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-2" for="start-dt">Start date/time:</label>
        <div class="col-sm-2">
            <input type="text" class="form-control" name="start-dt" id="start-dt" required>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-2" for="end-dt">End date/time:</label>
        <div class="col-sm-2">
            <input type="text" class="form-control" name="end-dt" id="end-dt" required>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-2" for="pres-id">Presenter:</label>
        <div class="col-sm-5">
            <select class="form-control" name="pres-id" id="pres-id">


                <option value="1">Perwaize Ahmed</option>

                <option value="2">Matt Granum</option>

                <option value="3">Scott Langnas</option>

                <option value="4">Mike Molenda</option>

                <option value="52">A A</option>


            </select>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-2" for="capacity">Event capacity:</label>
        <div class="col-sm-2">
            <input type="number" min="0" class="form-control" name="capacity" id="capacity">
        </div>
    </div>

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <input type="checkbox" name="open-reg" id="open-reg"> Allow open registration
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-2" for="reg-code">Registration code:</label>
        <div class="col-sm-2">
            <input type="text" class="form-control" name="reg-code" id="reg-code" maxlength="8">
        </div>
    </div>

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <input type="checkbox" name="survey-req" id="survey-req"> Require survey completion to record attendance
        </div>
    </div>

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <input type="submit" value="Create Event" class="btn btn-success">
        </div>
    </div>

</form>

<hr>

<button onclick="list-events" class="btn btn-primary">Back</button>

</div><!--container-->

<script type="text/javascript">
    $(function () {
        $('#start-dt').datetimepicker({
            format: 'YYYY-MM-DD HH:mm:ss',
            minDate: new Date(),
        });
    });

    $(function () {
        $('#end-dt').datetimepicker({
            format: 'YYYY-MM-DD HH:mm:ss',
            minDate: new Date(),
        });
    });
</script>

</body>
</html>