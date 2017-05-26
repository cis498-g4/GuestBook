<!-- JSP header using locally-hosted JS libraries -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${pageTitle}</title>
    <!-- jQuery 3.2.1 -->
    <script src="${pageContext.request.contextPath}/weblib/jquery-3.2.1/jquery-3.2.1.min.js"></script>
    <!-- Bootstrap 3.3.7 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/weblib/bootstrap-3.3.7-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/weblib/bootstrap-3.3.7-dist/css/bootstrap-theme.min.css">
    <script src="${pageContext.request.contextPath}/weblib/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <!-- DataTables 1.10.15 -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/weblib/datatables-1.10.15/datatables.min.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/weblib/datatables-1.10.15/datatables.min.js"></script>
    <!-- Bootstrap 3 DateTimePicker 4.17.47 -->
    <script type="text/javascript" src="${pageContext.request.contextPath}/weblib/datetimepicker-4.17.47/moment.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/weblib/datetimepicker-4.17.47/bootstrap-datetimepicker.min.js"></script>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/weblib/datetimepicker-4.17.47/bootstrap-datetimepicker.min.css">
    <!-- jQuery Bar Rating 1.2.2 -->
    <script type="text/javascript" src="${pageContext.request.contextPath}/weblib/bar-rating-1.2.2/jquery.barrating.min.js"></script>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/weblib/bar-rating-1.2.2/fontawesome-stars-o.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/weblib/font-awesome-4.7.0/css/font-awesome.min.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/default.css">
</head>
<body>

<div class="jumbotron">
    <h1><a href="home">Guestbook</a></h1>
</div>

<jsp:include page="nav.jsp"></jsp:include>

<div class="container" id="main-content">

    <c:if test="${statusMessage.length() > 0}">
    <div class="alert alert-${statusType != null ? statusType : 'info'} alert-dismissable}">
        <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
            ${statusMessage}
    </div>
    </c:if>

    <h2 class="page-title text-center">${pageTitle}</h2>