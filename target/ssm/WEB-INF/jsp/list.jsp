<%--
  Created by IntelliJ IDEA.
  User: chenxiaobing
  Date: 2020/5/10
  Time: 21:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>所有书籍</title>
</head>
<body>
    <table border="1">
        <tr>
            <th>书籍ID</th>
            <th>书籍名称</th>
            <th>书籍库存</th>
        </tr>
        <tr>
            <c:forEach items="${list}" var="book">
                <tr>
                    <td>${book.bookId}</td>
                    <td>${book.name}</td>
                    <td>${book.number}</td>
                </tr>
            </c:forEach>
        </tr>
    </table>

</body>
</html>
