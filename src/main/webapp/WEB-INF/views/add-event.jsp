<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<div class="spacer_1em"></div>

<!-- TODO: HTML / JS form validation -->
<form class="form-horizontal" action="add-event" method="post">
    <div class="row padding-horiz-10px">
        <div class="col-sm-offset-1">
            <div class="form-group">
                <label class="control-label col-sm-3" for="name">Event Name:</label>
                <div class="col-sm-5">
                    <input type="text" class="form-control" name="name" id="name" required>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-sm-3" for="start-dt">Start date/time:</label>
                <div class="col-sm-4">
                    <input type="text" class="form-control" name="start-dt" id="start-dt" required>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-sm-3" for="end-dt">End date/time:</label>
                <div class="col-sm-4">
                    <input type="text" class="form-control" name="end-dt" id="end-dt" required>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-sm-3" for="pres-id">Presenter:</label>
                <div class="col-sm-5">
                    <select class="form-control" name="pres-id" id="pres-id">

                        <c:forEach items="${organizers}" var="organizer">
                            <option value="${organizer.id}">${organizer.firstName} ${organizer.lastName}</option>
                        </c:forEach>

                    </select>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-sm-3" for="capacity">Event capacity:</label>
                <div class="col-sm-3">
                    <input type="number" min="0" max="10000" class="form-control" name="capacity" id="capacity">
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-3 col-sm-8">
                    <input type="checkbox" name="open-reg" id="open-reg"> Allow open registration
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-sm-3" for="reg-code">Registration code:</label>
                <div class="col-sm-3">
                    <input type="text" class="form-control" name="reg-code" id="reg-code" maxlength="8">
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-3 col-sm-8">
                    <input type="checkbox" name="survey-req" id="survey-req"> Require survey completion to record attendance
                </div>
            </div>

        </div>
    </div>

    <div class="spacer_1em"></div>

    <div class="form-group text-center">
        <a class="btn btn-primary" href="list-events">Cancel</a>
        <input type="submit" value="Create Event" class="btn btn-success">
    </div>

</form>

</div><!--container-->

<script type="text/javascript">
    $(function () {
        $('#start-dt').datetimepicker({
            format: 'YYYY-MM-DD HH:mm:ss',
            minDate: new Date()
        });

        $('#end-dt').datetimepicker({
            format: 'YYYY-MM-DD HH:mm:ss',
            useCurrent: false
        });

        // end-dt should always be after start-dt
        $('#start-dt').on('dp.change', function (e) {
            $('#end-dt').data('DateTimePicker').minDate(e.date);
        });

        $('#end-dt').on('dp.change', function (e) {
            $('#start-dt').data('DateTimePicker').maxDate(e.date);
        });

    });

</script>

</body>
</html>