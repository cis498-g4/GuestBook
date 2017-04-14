<jsp:include page="/templates/header.jsp"></jsp:include>

<table>
    <tr>

        <c:forTokens items="Last Name,First Name,Email Address,User Type" delims="," var="col_head">
        <th>${col_head}</th>
        </c:forTokens>

    </tr>

    <c:forEach items="${users}" var="user">
    <tr>
        <td>${user.lastName}</td>
        <td>${user.firstName}</td>
        <td>${user.email}</td>
        <td>${user.type == 'ORGANIZER' ? 'Organizer' : 'Guest'}</td>

        <c:forTokens items="view,edit,delete" delims="," var="action">
        <td>
            <form action="${action}-user">
                <input type="hidden" name="id" value="${user.id}">
                <input type="submit" value="${action}">
            </form>
        </td>
        </c:forTokens>

    </tr>
    </c:forEach>

</table>

</body>
</html>
