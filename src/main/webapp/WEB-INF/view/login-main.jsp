<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.lang}">
<head>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <!------ Include the above in your HEAD tag ---------->

    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.8/css/all.css">
    <title>Login</title>
    <style>
        <%@include file="../../../resources/static/css/styles.css" %>
    </style>
</head>
<body>
<article class="card-body mx-auto" style="max-width: 400px;">
    <form action="/controller?command=loginMain" method="post">
        <label class="errorMessage">${message}</label>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-lock"></i> </span>
            </div>
            <input name="username" class="form-control" placeholder="<fmt:message key="login.username"/>" type="username" value="${username}">
        </div>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-lock"></i> </span>
            </div>
            <input name="password" class="form-control" placeholder="<fmt:message key="login.password"/>" type="password" value="${password}">
        </div>
        <div class="form-group">
            <button style="height: 50px" type="submit" class="btn btn-primary btn-block"><fmt:message key="login.login"/></button>
        </div>
    </form>
</article>
</body>
</html>
