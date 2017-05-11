<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<div class="spacer_1em"></div>

<div class="text-center">
    <c:if test="${surveyTaken}">
        <p>You have already submitted this survey. Thank you for your response!</p>
    </c:if>

    <c:if test="${!surveyTaken}">
        <p>
            Please provide a rating for each question below, where a rating of 10 is the most positive.<br>
            You must respond to every question in order to submit the survey.
        </p>
    </c:if>

</div>

<div class="spacer_1em"></div>

<c:if test="${!surveyTaken}">

    <!-- TODO: HTML / JS form validation -->
    <form class="form-vertical" action="take-survey" method="post">
        <c:forEach begin="0" end="9" varStatus="outer">

            <div class="form-group text-center" id="${responses[outer.index]}">
                <label>${questions[outer.index]}</label>
                    <select class="form-control" name="${responses[outer.index]}" required>
                        <option value=""></option>
                        <c:forEach begin="1" end="10" varStatus="inner">
                            <option value="${inner.index}">${inner.index}</option>
                        </c:forEach>
                    </select>

                <span class="null-warning">Needs rating</span>

                <span class="your-rating hidden">
                    Your rating: <span class="value"></span>
                </span>

            </div>

        </c:forEach>

        <div class="spacer_1em"></div>

        <div class="hr_1em"></div>

        <div class="form-group text-center">
            <input type="hidden" name="eventId" value="${event.id}">
            <a class="btn btn-primary" href="${back}">Back</a>
            <input type="submit" class="btn btn-success" value="Submit Survey">
        </div>

    </form>

</c:if>


<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>

<script src="${pageContext.request.contextPath}/scripts/survey-barrating.js"></script>

</body>
