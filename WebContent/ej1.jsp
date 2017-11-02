<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page
	import="java.util.HashMap,java.util.Set" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%
		Object losEnlaces = request.getAttribute("enlaces");
		HashMap<String,String> enlaces = (HashMap<String,String>) losEnlaces;
		if(losEnlaces == null) {
	%>
	<p>Error, no se han mandado URL's</p>
	<%
		} else {
			if(enlaces.size() == 0) {
	%>
	<p>Error, no se han mandado URL's</p>
	<%
			} else {
				Set <String> keys = enlaces.keySet();
	%>
	<ul>
	<c:forEach var="item" items="${enlaces}">
		<li><a href="${item.value}"><c:out value="${item.key}"></c:out></a></li>
	</c:forEach>
	</ul>
	<%
			}
		}
	%>
</body>
</html>