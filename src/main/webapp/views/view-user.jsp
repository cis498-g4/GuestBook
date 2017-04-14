<jsp:include page="/templates/header.jsp"></jsp:include>

<table>
    <tr>
        <th>First Name:</th>
        <td>${user.firstName}</td>
    </tr>
    <tr>
        <th>Last Name:</th>
        <td>${user.lastName}</td>
    </tr>
    <tr>
        <th>Email Address:</th>
        <td>${user.email}</td>
    </tr>
    <tr>
        <th>User Type:</th>
        <td>${user.type == 'ORGANIZER' ? 'Organizer' : 'Guest'}</td>
    </tr>
</table>

<hr>

<table>
    <tr>
        <td><button onclick="history.go(-1)">back</button></td>

        <c:forTokens items="edit,delete" delims="," var="action">
        <td>
            <form action="${action}-user">
                <input type="hidden" name="id" value="${user.id}">
                <input type="submit" value="${action}">
            </form>
        </td>
        </c:forTokens>

    </tr>
</table>

</body>
</html>
