<%--
  Created by IntelliJ IDEA.
  User: karan
  Date: 5/16/2019
  Time: 4:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <title>
        <sec:authorize access="hasRole('ROLE_ADMIN')">
            Admin
        </sec:authorize>Home
    </title>
</head>
<body>
Admin Home
</body>
</html>
