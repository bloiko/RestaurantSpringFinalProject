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
    <title>Input person information</title>
    <style><%@include file="/WEB-INF/css/styles.css"%></style>
</head>
<body>
<article class="card-body mx-auto" style="max-width: 400px;">
    <h4 class="card-title mt-3 text-center"><fmt:message key="registration.registration"/></h4>
    <form action="/controller?command=registration" method="post">
        <label class="errorMessage">${error_message}</label>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-user"></i> </span>
            </div>
            <input name="first_name" class="form-control" placeholder="<fmt:message key="registration.first_name"/>" value="${first_name}" type="text">
        </div>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-user"></i> </span>
            </div>
            <input name="last_name" class="form-control" placeholder="<fmt:message key="registration.last_name"/>" type="text" value="${last_name}">
        </div>
        <label class="errorMessage">${username_error_message}</label>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-lock"></i> </span>
            </div>
            <input name="username" class="form-control" placeholder="<fmt:message key="registration.username"/>" type="text">
        </div>
        <label class="errorMessage">${password_error_message}</label>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-lock"></i> </span>
            </div>
            <input name="password" class="form-control" placeholder="<fmt:message key="registration.password"/>" type="password">
        </div>
        <label class="errorMessage">${email_error_message}</label>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-envelope"></i> </span>
            </div>
            <input name="email" class="form-control" placeholder="<fmt:message key="registration.email"/>" type="text" value="${email}">
        </div>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-building"></i> </span>
            </div>
            <input name="address" class="form-control" placeholder="<fmt:message key="registration.address"/>" type="text" value="${address}">
        </div>
        <label class="errorMessage">${phone_number_error_message}</label>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-phone"></i> </span>
            </div>
            <input name="phoneNumber" class="form-control" placeholder="<fmt:message key="registration.phone_number"/>" type="text" value="${phoneNumber}">
        </div>
        <div class="form-group">
            <button type="submit" class="btn btn-primary btn-block"><fmt:message key="registration.submit"/></button>
        </div>
    </form>
</article>
</body>
</html>
