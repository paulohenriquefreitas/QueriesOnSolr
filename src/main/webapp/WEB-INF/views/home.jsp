<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html >
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Solr Queries Simplifying</title>
<link type="text/css" href="/resources/css/bootstrap.css" rel="stylesheet"/>
<link type="text/css" href="/resources/css/hover.css" rel="stylesheet"/>
<script src="http://code.jquery.com/jquery.min.js"></script>
<script src="/resources/js/bootstrap.min.js"></script>

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
	
.col{
margin-bottom: 0px;
padding-bottom: 5px;
padding-left: 7px;
border-radius: 8px;
border: 2px solid #e3e3e3;
}

.col2{
margin-bottom: 0px;
padding-bottom: 5px;
padding-left: 7px;
border-radius: 8px;
width: 75%;
border: 2px solid #e3e3e3;
}

.col-wrap{
overflow: hidden; 
}
	
</style> 
</head>
<body >
		<%-- <jsp:include page="/WEB-INF/views/header.jsp" /> --%>
		
		<div class="container">
	<div class="row clearfix">
		<div class="col-md-12 column">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">
						<img alt="40x40" src="/resources/image/solr.png" class="img-rounded" />
						<span>QueriesOnSolr</span>
					</h3>
				</div>
			</div>
		</div>
	</div>
</div>
					
		<form:form class="form-horizontal"  action="/busca" method="post" modelAttribute="query" >
		<div class="container">
		<div class="row clearfix">
			<div class="col-md-12 column">
			    <div class="btn-group btn-group-justified font-group">
			      <div class="btn-group">
			        <a class="btn btn-primary dropdown-toggle" data-toggle="dropdown">
			          SUBMARINO <span class="caret"></span>
			        </a>
			        <ul class="dropdown-menu" role="menu">
			          <li><a href="#">Produção</a></li>
			          <li><a href="#">Staging</a></li>			         
			        </ul>
			      </div>
			      <div class="btn-group">
			        <a class="btn btn-danger dropdown-toggle" data-toggle="dropdown">
			          AMERICANAS <span class="caret"></span>
			        </a>
			         <ul class="dropdown-menu" role="menu">
			          <li><a href="#">Produção</a></li>
			          <li><a href="#">Staging</a></li>			         
			        </ul>
			      </div>
			      <a class="btn btn-shop" role="button">SHOPTIME</a>
			      <a class="btn btn-soub" role="button">SOUBARATO</a>
			      <a class="btn btn-success" role="button">HOMOLOGAÇÃO</a>
			    </div>
			</div>
		</div>
	</div>
	
	<div class="container">
	<div class="row clearfix">
		<div class="col-md-4 col">
				<label class="control-label">Item Id</label>
				<div class="controls ">
					<form:input class="form-control" placeholder="Pesquisa por Id"  path="id"/>
				</div>
				
				<label class="control-label">Números de Parceiros</label>
				<div class="controls ">
					<form:input class="form-control" placeholder="Pesquisa por Id"  path="numPartner"/>
				</div>
				
				<label class="control-label">Números de Skus</label>
				<div class="controls ">
					<form:input class="form-control" placeholder="Pesquisa por Id"  path="numSkus"/>
				</div>
				<label class="control-label" >Origem do Item:</label>
				<form:radiobutton class="radio-inline" path="type" value="100"/>100%
				<form:radiobutton class="radio-inline" path="type" value="misto"/>misto
				<form:radiobutton class="radio-inline" path="type" value="b2w"/>b2w
				<br/>
				
				<label class="control-label" >Estoque:</label>
				<form:radiobutton class="radio-inline" path="stock" value="10"/>true
				<form:radiobutton class="radio-inline" path="stock" value="false"/>false
				<br/>
				
				<label class="control-label" >Moda:   </label>
				<form:radiobutton class="radio-inline" path="fashion" value="10"/>true
				<form:radiobutton class="radio-inline" path="fashion" value="false"/>false
				<br/>
				
				<label class="control-label">Campos</label>	<br/>				
				<form:select  class="checkbox" path="fields" items="${fields}"> 		
	       		 </form:select>
	       		 	  
				<div class="form-actions"><br/>
					<button type="submit" class="btn btn-success">Submit</button>
					<button type="button" class="btn">Cancel</button>
				</div>	
					
		</div>
		<div class="col-md-4 col2">
		<label class="control-label" >Resultado da query:</label><br/>
					<pre>${itemList}</pre>
		</div>
	</div>
</div>
			
		</form:form>
</body>
</html>