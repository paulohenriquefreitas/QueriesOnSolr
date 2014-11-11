<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

					 <c:if test="${fn:length(itemList) gt 0}">
						<div class="col-md-4 col2">
								<label class="control-label" >Resultado da query: ${size}</label><br/>
								
								<c:forEach var="item" items="${idList}">
								  <a href="${link}${item.id}" target="_blank">${item.id}</a>
								</c:forEach>
								
								<pre>${itemList}</pre>		
								
						</div>
					</c:if>

</body>
</html>