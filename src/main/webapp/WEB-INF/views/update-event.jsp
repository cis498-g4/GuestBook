<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<!-- TODO: HTML / JS form validation -->
<form class="form-horizontal" action="update-event" method="post">
    <div class="form-group">
        <label class="control-label col-sm-2" for="name">Event Name:</label>
        <div class="col-sm-5">
            <input type="text" class="form-control" name="name" id="name" value="${event.name}" required>
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

                <c:forEach items="${organizers}" var="organizer">
                    <option value="${organizer.id}" ${event.presenter.id == organizer.id ? "selected" : ""}>
                            ${organizer.firstName} ${organizer.lastName}
                    </option>
                </c:forEach>

            </select>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-2" for="capacity">Event capacity:</label>
        <div class="col-sm-2">
            <input type="number" min="0" max="10000" class="form-control" name="capacity" id="capacity" value="${event.capacity > 0 ? event.capacity : ''}">
        </div>
    </div>

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <input type="checkbox" name="open-reg" id="open-reg" ${event.openRegistration ? "checked" : ""}> Allow open registration
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-2" for="reg-code">Registration code:</label>
        <div class="col-sm-2">
            <input type="text" class="form-control" name="reg-code" id="reg-code" maxlength="8" value="${event.registrationCode != null ? event.registrationCode : ''}">
        </div>
    </div>

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <input type="checkbox" name="survey-req" id="survey-req" ${event.mandatorySurvey ? "checked" : ""}> Require survey completion to record attendance
        </div>
    </div>

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <input type="hidden" name="id" value="${event.id}">
            <input type="submit" value="Update Event Information" class="btn btn-success">
        </div>
    </div>

</form>

<hr>

<button onclick="history.go(-1)" class="btn btn-primary">Back</button>

</div><!--container-->

<script type="text/javascript">
    $(function () {
        var startDt = new Date('${startDt}');
        var endDt = new Date('${endDt}');
        var minDate = new Date();

        if (startDt.getTime() < minDate) {
            minDate = startDt;
        }

        $('#start-dt').datetimepicker({
            format: 'YYYY-MM-DD HH:mm:ss',
            minDate: minDate,
            defaultDate: startDt
        });

        $('#end-dt').datetimepicker({
            format: 'YYYY-MM-DD HH:mm:ss',
            useCurrent: false,
            defaultDate: endDt

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