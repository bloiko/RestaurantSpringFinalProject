<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<%@ page session="true" %>


<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.lang}">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/flag-icon-css/3.1.0/css/flag-icon.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

    <title>Cart</title>
    <style>
        <%@include file="/WEB-INF/css/styles.css" %>
    </style>
</head>
<body>
<div class="bs-example">
    <nav class="navbar navbar-expand-md navbar-light bg-light">
        <a href="#" class="navbar-brand">
            <img width="70" height="70"
                 src="https://image.similarpng.com/thumbnail/2020/06/Restaurant-logo-with-chef-drawing-template-on-transparent-background-PNG.png"
                 height="28" alt="5 Min Cafe">
        </a>
        <button type="button" class="navbar-toggler" data-toggle="collapse" data-target="#navbarCollapse">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarCollapse">
            <div class="navbar-nav">
                <a href="/controller?command=menuList" class="nav-item nav-link"><fmt:message
                        key="header.menu"/></a>
                <c:if test="${sessionScope.get('username')!=null}">
                    <a href="cart.jsp" class="nav-item nav-link active"><fmt:message key="header.cart"/></a>
                    <a href="/controller?command=myOrders" class="nav-item nav-link"><fmt:message
                            key="header.my_orders"/></a>
                </c:if>
            </div>
            <div class="nav-item dropdown ml-auto">
                <a class="nav-link dropdown-toggle" href="" id="dropdown09" style="color:black;" data-toggle="dropdown"
                   aria-haspopup="true" aria-expanded="false">
                    <c:if test="${sessionScope.lang!='ua'}">
                        <span class="flag-icon flag-icon-us"> </span> <fmt:message
                            key="header.language"/></a>
                    </c:if>
                    <c:if test="${sessionScope.lang=='ua'}">
                    <span class="flag-icon flag-icon-${sessionScope.lang}"> </span> <fmt:message
                        key="header.language"/></a>
                    </c:if>
                <div class="dropdown-menu" aria-labelledby="dropdown09">
                    <a class="dropdown-item" href="/controller?command=cartList&sessionLocale=ua"><span
                            class="flag-icon flag-icon-ua"> </span> <fmt:message key="header.ukrainian"/></a>
                    <a class="dropdown-item" href="/controller?command=cartList&sessionLocale=en"><span
                            class="flag-icon flag-icon-us"> </span><fmt:message key="header.english"/></a>
                </div>
            </div>
            <c:if test="${sessionScope.get('username')==null}">
                <div class="navbar-nav">
                    <a href="registration.jsp" class="nav-item nav-link"></span><fmt:message
                            key="header.registration"/></a>
                    <a href="login-main.jsp" class="nav-item nav-link"></span><fmt:message key="header.login"/></a>
                </div>
            </c:if>
            <c:if test="${sessionScope.get('username')!=null}">
                <div class="navbar-nav">
                    <a href="/controller?command=logout" class="nav-item nav-link"></span><fmt:message key="header.logout"/></a>
                </div>
                <a href="cart.jsp">
            <span class="fa-stack fa-2x has-badge" data-count="${sessionScope.cart.size()}">
                    <i class="fa fa-circle fa-stack-2x fa-inverse"></i>
                    <i style="" class="fa fa-shopping-cart fa-stack-2x red-cart"></i>
                </span>
                </a>
            </c:if>
        </div>
    </nav>
</div>
<div id="header">
    <h2><fmt:message key="cart.cart"/></h2>
</div>
<div id="container">
    <div id="content">
        <table class="table">
            <thead></thead>
            <tr>
                <th scope="col"><fmt:message key="cart.image"/></th>
                <th scope="col"><fmt:message key="cart.name"/></th>
                <th scope="col"><fmt:message key="cart.price"/></th>
                <th scope="col"><fmt:message key="cart.quantity"/></th>
                <th scope="col"><fmt:message key="cart.option"/></th>
                <th scope="col"><fmt:message key="cart.sub_total"/></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${sessionScope.cart}">
                <c:set var="sum" value="${sum+item.foodItem.price*item.quantity}"/>
                <tr>
                    <td><img src="${item.foodItem.image}" width="80" height="80"></td>
                    <c:if test="${sessionScope.lang!='ua'}">
                        <td> ${item.foodItem.name} </td>
                    </c:if>
                    <c:if test="${sessionScope.lang=='ua'}">
                        <td> ${item.foodItem.nameUa} </td>
                    </c:if>
                    <td> ${item.foodItem.price}$</td>
                    <td> ${item.quantity} </td>
                    <td align="left"><a href="/controller?command=cartDeleteItem&itemId=${item.foodItem.id}"
                                        onclick="return confirm('Are you sure?')">
                        <button type="button" class="btn btn-dark"><fmt:message key="cart.delete"/></button>
                    </a></td>
                    >
                    <td>${item.foodItem.price*item.quantity}$</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<div class="order" style="position: relative;">
    <h2><fmt:message key="cart.sum"/>: ${sum}$</h2>
    <a href="/controller?command=cartOrderItem">
        <button type="button" class="btn btn-danger order-button" style="position: absolute; right: 10%"><fmt:message
                key="cart.order"/></button>
    </a>
</div>
<br/><br/>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
