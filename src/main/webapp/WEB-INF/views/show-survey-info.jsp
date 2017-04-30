<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<table class="table table-condensed info-list" id="survey-info">
    <thead hidden></thead>
    <tbody>
        <tr>
            <td class="col-xs-3"><strong>Event Name:</strong></td>
            <td>${survey.event.name}</td>
        </tr>
        <tr>
            <td class="col-xs-3"><strong>Event Date:</strong></td>
            <td>${survey.event.startDateTime.getMonthValue()}/${survey.event.startDateTime.getDayOfMonth()}/${survey.event.startDateTime.getYear()}</td>
        </tr>
        </tr>
        <tr>
            <td class="col-xs-3"><strong>Presenter:</strong></td>
            <td>${survey.event.presenter.firstName} ${survey.event.presenter.lastName}</td>
        </tr>
        <tr>
            <td class="col-xs-3"><strong>Reviewer Name:</strong></td>
            <td>${survey.user.firstName} ${survey.user.lastName}</td>
        </tr>
        <tr>
            <td class="col-xs-3"><strong>Submission Date:</strong></td>
            <td>
                ${survey.submissionDateTime.getMonthValue()}/${survey.submissionDateTime.getDayOfMonth()}/${survey.submissionDateTime.getYear()}
                ${survey.submissionDateTime.getHour()}:${survey.submissionDateTime.getMinute() < 10 ? "0" : ""}${survey.submissionDateTime.getMinute()}
            </td>
        </tr>
    </tbody>
</table>

<hr>

<table class="table table-condensed table-striped info-list" id="survey-results">
    <thead hidden></thead>
    <tbody>
        <tr hidden>
            <td>Event Name</td>
            <td>${survey.event.name}</td>
        </tr>
        <tr hidden>
            <td>Event Date</td>
            <td>${survey.event.startDateTime.getMonthValue()}/${survey.event.startDateTime.getDayOfMonth()}/${survey.event.startDateTime.getYear()}</td>
        </tr>
        </tr>
        <tr hidden>
            <td>Presenter</td>
            <td>${survey.event.presenter.firstName} ${survey.event.presenter.lastName}</td>
        </tr>
        <tr hidden>
            <td>Reviewer Name</td>
            <td>${survey.user.firstName} ${survey.user.lastName}</td>
        </tr>
        <tr hidden>
            <td>Submission Date</td>
            <td>
                ${survey.submissionDateTime.getMonthValue()}/${survey.submissionDateTime.getDayOfMonth()}/${survey.submissionDateTime.getYear()}
                ${survey.submissionDateTime.getHour()}:${survey.submissionDateTime.getMinute() < 10 ? "0" : ""}${survey.submissionDateTime.getMinute()}
            </td>
        </tr>

        <tr>
            <td>Overall, how would you rate your satisfaction with the event?</td>
            <td>${survey.responses.get("response_01")}</td>
        </tr>
        <tr>
            <td>How likely are you to recommend the event to a friend or colleague?</td>
            <td>${survey.responses.get("response_02")}</td>
        </tr>
        <tr>
            <td>How satisfied were you with the quality of the information presented?</td>
            <td>${survey.responses.get("response_03")}</td>
        </tr>
        <tr>
            <td>How relevant did you find the information in the presentation?</td>
            <td>${survey.responses.get("response_04")}</td>
        </tr>
        <tr>
            <td>How satisfied are you with the presenter's knowledge of the subject?</td>
            <td>${survey.responses.get("response_05")}</td>
        </tr>
        <tr>
            <td>How satisfied are you with the quality of the presentation?</td>
            <td>${survey.responses.get("response_06")}</td>
        </tr>
        <tr>
            <td>How likely are you to attend another event by the same presenter?</td>
            <td>${survey.responses.get("response_07")}</td>
        </tr>
        <tr>
            <td>How satisfied were you with the organization of the event?</td>
            <td>${survey.responses.get("response_08")}</td>
        </tr>
        <tr>
            <td>How satisfied were you with the staff at the event?</td>
            <td>${survey.responses.get("response_09")}</td>
        </tr>
        <tr>
            <td>How well did we respond to your questions and concerns?</td>
            <td>${survey.responses.get("response_10")}</td>
        </tr>

        <tr hidden>
            <td>Average rating</td>
            <td>${average}</td>
        </tr>
        <tr hidden>
            <td>Sentiment</td>
            <td>${sentiment}</td>
        </tr>

    </tbody>
</table>

<hr>

<p>Overall, this survey is <strong>${sentiment}</strong>, with an average rating of <strong>${average}</strong></p>

<hr>

<table>
    <tr>
        <td><button onclick="history.go(-1)">back</button></td>
        <td><button onclick="">download CSV</button></td>
    </tr>
</table>

</div><!--container-->

<script>

    $(document).ready(function() {
        var table = $('#survey-results').DataTable( {
            dom: '<"row"<"col-sm-12"rt>>' +
            '<"dt_spacer_20">' +
            '<"dt_hr_20">' +
            '<"row"<"col-sm-12"B>>',
            paging: false,
            order: [],
            columnDefs: [ { orderable: false, targets: [0,1] } ],
            buttons: [
                {
                    text: 'Back',
                    className: 'btn-primary',
                    action: function ( e, dt, node, conf ) {
                        window.location.href = 'list-surveys';
                    }
                },
                { extend: 'csv', text: 'Download CSV', className: 'btn-primary' },
                { extend: 'print', className: 'btn-primary'}
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