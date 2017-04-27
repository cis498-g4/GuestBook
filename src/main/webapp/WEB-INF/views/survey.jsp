<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<c:if test="${surveyTaken}">
    <p>You have already submitted this survey. Thank you for your response!</p>
</c:if>

<c:if test="${!surveyTaken}">
    <p>
        Please provide a response on the scale of 1 to 10 for each question below, where 10 represents the most positive response.
        <br>You must respond to every question in order for your survey to be counted.
    </p>

    <!-- TODO: HTML / JS form validation -->
    <form action="take-survey" method="post">


        <c:forEach begin="0" end="9" varStatus="outer">
            <p><strong>${questions[outer.index]}</strong></p>
            <table>
                <tr>
                    <td>Not ${responseTypes[outer.index]}</td>
                    <c:forEach begin="1" end="10" varStatus="inner">
                        <td><input type="radio" name="${responses[outer.index]}" value="${inner.index}" required></td>
                    </c:forEach>
                    <td>Very ${responseTypes[outer.index]}</td>
                </tr>
                <tr>
                    <td></td>
                    <c:forEach begin="1" end="10" varStatus="num">
                        <td>${num.index}</td>
                    </c:forEach>
                    <td></td>
                </tr>
            </table>
        </c:forEach>


        <input type="hidden" name="eventId" value="${event.id}">
        <input type="submit" value="submit survey">
    </form>
</c:if>

<hr>

<button onclick="history.go(-1)">back</button>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>

