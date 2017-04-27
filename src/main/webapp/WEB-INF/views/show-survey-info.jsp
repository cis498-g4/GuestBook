<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

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
    <c:forEach items="${surveyResponses}" var="response">
        <tr>
            <td>${response.key}</td>
            <td><strong>${response.value}</strong></td>
        </tr>
    </c:forEach>
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

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>
