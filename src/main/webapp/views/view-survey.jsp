<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/templates/header.jsp"></jsp:include>

<table>
    <tr>
        <td><strong>Event Name:</strong></td>
        <td>${survey.event.name}</td>
    </tr>
    <tr>
        <td><strong>Event Date:</strong></td>
        <td>${survey.event.startDateTime.getMonthValue()}/${survey.event.startDateTime.getDayOfMonth()}/${survey.event.startDateTime.getYear()}</td>
    </tr>
    </tr>
    <tr>
        <td><strong>Presenter:</strong></td>
        <td>${survey.event.presenter.firstName} ${survey.event.presenter.lastName}</td>
    </tr>
    <tr>
        <td><strong>Reviewer Name:</strong></td>
        <td>${survey.user.firstName} ${survey.user.lastName}</td>
    </tr>
    <tr>
        <td><strong>Submission Date:</strong></td>
        <td>
            ${survey.submissionDateTime.getMonthValue()}/${survey.submissionDateTime.getDayOfMonth()}/${survey.submissionDateTime.getYear()}
            ${survey.submissionDateTime.getHour()}:${survey.submissionDateTime.getMinute() < 10 ? "0" : ""}${survey.submissionDateTime.getMinute()}
        </td>
    </tr>
</table>

<hr>

<table>
    <tr>
        <td><strong>Question 1 goes here: </strong></td>
        <td>${survey.responses.get("response_01")}</td>
    </tr>
    <tr>
        <td><strong>Question 2 goes here: </strong></td>
        <td>${survey.responses.get("response_02")}</td>
    </tr>
    <tr>
        <td><strong>Question 3 goes here: </strong></td>
        <td>${survey.responses.get("response_03")}</td>
    </tr>
    <tr>
        <td><strong>Question 4 goes here: </strong></td>
        <td>${survey.responses.get("response_04")}</td>
    </tr>
    <tr>
        <td><strong>Question 5 goes here: </strong></td>
        <td>${survey.responses.get("response_05")}</td>
    </tr>
    <tr>
        <td><strong>Question 6 goes here: </strong></td>
        <td>${survey.responses.get("response_06")}</td>
    </tr>
    <tr>
        <td><strong>Question 7 goes here: </strong></td>
        <td>${survey.responses.get("response_07")}</td>
    </tr>
    <tr>
        <td><strong>Question 8 goes here: </strong></td>
        <td>${survey.responses.get("response_08")}</td>
    </tr>
    <tr>
        <td><strong>Question 9 goes here: </strong></td>
        <td>${survey.responses.get("response_09")}</td>
    </tr>
    <tr>
        <td><strong>Question 10 goes here: </strong></td>
        <td>${survey.responses.get("response_10")}</td>
    </tr>
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

</body>
</html>
