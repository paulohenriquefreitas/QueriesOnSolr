<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>	


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Solr Queries Simplifying</title>
<link type="text/css" href="/resources/css/bootstrap.css" rel="stylesheet"/>
<link type="text/css" href="/resources/css/hover.css" rel="stylesheet"/>

<style type="text/css">
	.imagem  {
	background-image: url("img/apache-solr.jpg");
	
	}	
	.transparencia {
	     filter:alpha(opacity=50);
	     opacity: 0.5;
	     -moz-opacity:0.5;
	     -webkit-opacity:0.5;
	}	
	
	.verticalLine {
    border-left: thick solid #ff0000;
	}	
	

</style> 
</head>
<body >
		<jsp:include page="/WEB-INF/views/header.jsp" />
	
		<form:form class="form-horizontal"  action="/test" method="post" modelAttribute="query" >
			<div class="jumbotron">	
				<label class="control-label">Item Id</label>
				<div class="controls ">
					<form:input class="form-control" placeholder="Pesquisa por Id"  path="id"/>
				</div>
				<label class="control-label" >Estoque:</label><br/>
				<form:radiobutton class="radio-inline" path="stock" value="10"/>true
				<form:radiobutton class="radio-inline" path="stock" value="false"/>false
				<br/>
				<label class="control-label">Campos</label>	<br/>				
				<form:select  class="checkbox" path="fields" items="${phonesMap}"> 		
	       		 </form:select>  
				<div class="form-actions"><br/>
					<button type="submit" class="btn btn-success">Submit</button>
					<button type="button" class="btn">Cancel</button>
				</div>	
					<span style="background: red;">${indexedItem.itemName}</span>
					<span style="background: red;">${indexedItem.itemId}</span>
					<span style="background: green;">${indexedItem.authorNameList}</span>
				<div class="vertical-line" >
		</div>					
			</div>
		</form:form>
</body>
</html>