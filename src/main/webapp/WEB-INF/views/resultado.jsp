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
		<div class="col-md-12 ">
				<label class="control-label" >Resultado da query: ${size}</label><br/>
				<c:forEach var="item" items="${idList}">
				  <a href="${link}${item.id}" target="_blank">${item.id}</a>
				</c:forEach>
				<div class="panel-group" id="accordion"> 
			        <c:forEach var="item" items="${idList}">
				        <div class="panel panel-default">
				            <div class="panel-heading">
				                <h4 class="panel-title">
				                    <a data-toggle="collapse" data-parent="#accordion"href='<%= "#collapse_#{item.id}" %>'>${item.itemId} - ${item.itemName}</a>
				                </h4>
				            </div>
				            <div id='<%= "collapse_#{item.id}" %>' class="panel-collapse collapse in">
				                <div class="panel-body">
				                    <p>${itemList}</p>
				                </div>
				            </div>
				        </div> 
			        </c:forEach>
			    </div>
				
				
		</div>
	 </c:if>

</body>
</html>