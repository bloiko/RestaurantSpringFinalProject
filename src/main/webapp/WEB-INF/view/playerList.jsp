<%--
  Created by IntelliJ IDEA.
  User: Loiko
  Date: 11.01.2021
  Time: 17:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page isELIgnored="false" %>

<html>
<head>
    <title>List Players</title>

    <!-- reference our style sheet -->

    <link type="text/css"
          rel="stylesheet"
          href="${pageContext.request.contextPath}/resources/css/style.css" />
</head>
<body>
<div id="wrapper">
    <div id="header">
        <h2>PRM - Player Relationship Manager</h2>
    </div>
</div>
<div id="container">
    <div id="content">
        <table>
            <tr>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Email</th>
            </tr>
            <c:forEach items="${players}" var="player">
                <tr>
                    <td>${player.firstName}</td>
                    <td>${player.lastName}</td>
                    <%--<td>${player.team}</td>--%>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>

</body>
</html>
