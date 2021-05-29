<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div id="wrapper">
    <div id="header">
        <h2>Admin</h2>
    </div>
</div>
<div id="container">
    <div id="content">
        <table border="1">
            <tr>
                <th>id</th>
                <th>User information</th>
                <th>Date of ordering</th>
                <th>Items</th>
                <th>Order price</th>
                <th>Status</th>
            </tr>
            <c:forEach var="order" items="${NOT_DONE_ORDERS_LIST}">
                <c:set var="orderPrice" value="${0}"/>
                <tr>
                    <td>${order.id}</td>
                    <td>
                        <table>
                            <tr>
                                <td>Id</td>
                                <td> ${order.user.id} </td>
                            </tr>
                            <tr>
                                <td>First Name</td>
                                <td> ${order.user.firstName} </td>
                            </tr>
                            <tr>
                                <td>Last Name</td>
                                <td>${order.user.lastName}</td>
                            </tr>
                            <tr>
                                <td>Usename</td>
                                <td>${order.user.userName}</td>
                            </tr>
                            <tr>
                                <td>Email</td>
                                <td>${order.user.email}</td>
                            </tr>
                            <tr>
                                <td>Address</td>
                                <td>${order.user.address}</td>
                            </tr>
                            <tr>
                                <td>Phone Number</td>
                                <td>${order.user.phoneNumber}</td>
                            </tr>
                        </table>
                    </td>
                    <td>
                            ${order.orderDate}
                    </td>
                    <td>
                        <table>
                            <tr>
                                <th>Name</th>
                                <th>Price</th>
                                <th>Quantity</th>
                                <th>Sub Total</th>
                            </tr>


                            <c:forEach var="item" items="${order.items}">
                                <c:set var="orderPrice" value="${orderPrice+item.foodItem.price*item.quantity}"/>
                                <tr>
                                    <td> ${item.foodItem.name} </td>
                                    <td> ${item.foodItem.price}</td>
                                    <td>${item.quantity}</td>
                                    <td>${item.foodItem.price*item.quantity}</td>
                                </tr>
                            </c:forEach>
                        </table>
                    </td>
                    <td>${orderPrice}</td>
                    <td>
                            ${order.status}
                        <br/><br/>
                        <form action="/controller?command=changeOrderStatus&orderId=${order.id}" method="post">
                            <select name="status">
                                <c:forEach var="statusTemp" items="${statusList}">
                                    <option value="${statusTemp}">${statusTemp}</option>
                                </c:forEach>
                            </select>
                            <br/>
                            <input type="submit" value="Change"/>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>


        <h1>Done tasks:</h1>
        <table border="1">
            <tr>
                <th>id</th>
                <th>User information</th>
                <th>Date of ordering</th>
                <th>Items</th>
                <th>Order price</th>
                <th>Status</th>
            </tr>
            <c:forEach var="order" items="${DONE_ORDERS_LIST}">
                <c:set var="orderPrice" value="${0}"/>
                <tr>
                    <th>${order.id}</th>
                    <th>
                        <table>
                            <tr>
                                <td>First Name</td>
                                <td> ${order.user.firstName} </td>
                            </tr>
                            <tr>
                                <td>Last Name</td>
                                <td>${order.user.lastName}</td>
                            </tr>
                            <tr>
                                <td>Username</td>
                                <td>${order.user.userName}</td>
                            </tr>
                            <tr>
                                <td>Email</td>
                                <td>${order.user.email}</td>
                            </tr>
                            <tr>
                                <td>Address</td>
                                <td>${order.user.address}</td>
                            </tr>
                            <tr>
                                <td>Phone Number</td>
                                <td>${order.user.phoneNumber}</td>
                            </tr>
                        </table>
                    </th>
                    <th>${order.orderDate}</th>
                    <th>
                        <table>
                            <tr>
                                <th>Name</th>
                                <th>Price</th>
                                <th>Quantity</th>
                                <th>Sub Total</th>
                            </tr>


                            <c:forEach var="item" items="${order.items}">
                                <c:set var="orderPrice" value="${orderPrice+item.foodItem.price*item.quantity}"/>
                                <tr>
                                    <td> ${item.foodItem.name} </td>
                                    <td> ${item.foodItem.price}</td>
                                    <td>${item.quantity}</td>
                                    <td>${item.foodItem.price*item.quantity}</td>
                                </tr>
                            </c:forEach>
                        </table>
                    </th>
                    <th>${orderPrice}</th>
                    <th>
                            ${order.status}
                    </th>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>
</body>
</html>
