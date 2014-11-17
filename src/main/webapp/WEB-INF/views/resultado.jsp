<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
    <%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
    
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
			        <c:forEach var="item" items="${itemList}"> 
				         <div class="panel panel-default">
				            <div class="panel-heading">
				                <h4 class="panel-title">
				                    <a data-toggle="collapse" data-parent="#accordion" href="#${item.id}">${item.id} - ${item.itemName}</a>
				                </h4>
				            </div>
				            <div id="${item.id}" class="panel-collapse collapse">
				                <div class="panel-body">				                    
										<json:object>
											<json:property name="item" value="${item}"/>
										</json:object>
				                </div>
				            </div>
				        </div> 
			       </c:forEach> 
			    </div>			
		</div>
	 </c:if>	 
</body>
</html>