<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body>
<h2>This page shows list of all SNCF trips
</h2>

    <ul>
        <c:forEach var="listValue" items="${list}">
            <li>${listValue}</li>
        </c:forEach>
    </ul>

</body>
</html>