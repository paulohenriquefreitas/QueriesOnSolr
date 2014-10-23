<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html >
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Solr Queries Simplifying</title>
<link type="text/css" href="/resources/css/bootstrap.css" rel="stylesheet"/>
<link type="text/css" href="/resources/css/hover.css" rel="stylesheet"/>
<link type="text/css" href="/resources/css/bootstrap-select.css" rel="stylesheet"/>
<link type="text/css" href="/resources/css/toggle-switch.css" rel="stylesheet"/>
<link type="text/css" href="/resources/css/select2.css" rel="stylesheet"/>

<link href="bootstrap-switch.css" rel="stylesheet">
<script src="http://code.jquery.com/jquery.min.js"></script>
<script src="/resources/js/bootstrap.min.js"></script>
<script src="/resources/js/bootstrap-select.js"></script>
<script src="/resources/js/select2.js"></script>


<style type="text/css">
.btn-custom:active {
    top: 5px;
    border-bottom: 0;
}
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
			     <div id=mode-group class="btn-group btn-group-justified font-group " data-toggle="buttons">
			      <label class="btn btn-primary " ><form:radiobutton path="brand" value="submarino"  checked="checked"/>SUBMARINO</label>			        
			      <label class="btn btn-danger "><form:radiobutton path="brand" value="americanas" />AMERICANAS</label>			        
			      <label class="btn btn-shop " ><form:radiobutton path="brand" value="shoptime"/>SHOPTIME</label>
			      <label class="btn btn-soub" ><form:radiobutton path="brand" value="soubarato"/>SOUBARATO</label>
			      <label class="btn btn-success" ><form:radiobutton path="brand" value="hml" />HOMOLOGAÇÃO</label>
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
						<form:input class="form-control" placeholder="Quantidade de parceiros"  path="numPartner"/>
					</div>
					
					<label class="control-label">Números de Skus</label>
					<div class="controls ">
						<form:input class="form-control" placeholder="Quantidade de Skus"  path="numSkus"/>
					</div>
					<label class="control-label" >Tipo:</label>
					<form:radiobutton class="radio-inline " path="type" value="b2w" checked="checked"  />b2w
					<form:radiobutton class="radio-inline" path="type" value="100"/>100%
					<form:radiobutton class="radio-inline" path="type" value="misto"/>misto
					<br/>
					
					<label class="control-label" >Estoque:</label>
					<form:radiobutton class="radio-inline" path="stock" value="true" checked="checked" />true
					<form:radiobutton class="radio-inline" path="stock" value="false"/>false
					
					 <ul class="list-inline">
						 <li>
					      <div class="btn-group btn-toggle"> 
					    	<button class="btn btn-xs btn-default" path="stock" value="true">TRUE</button>
					    	<button class="btn btn-xs btn-info active" path="stock" value="false">FALSE</button>
					      </div>
					    </li>
				    </ul>
					
					<label class="control-label" >Moda:   </label>
					<form:radiobutton class="radio-inline" path="fashion" value="10"/>true
					<form:radiobutton class="radio-inline" path="fashion" value="false"/>false
					<br/>
					
					<div class="switch-toggle well">
					  <input id="week" name="view" type="radio" checked>
					  <label for="week" onclick="">Week</label>
					
					  <input id="month" name="view" type="radio">
					  <label for="month" onclick="">Month</label>
					
					  <a class="btn btn-primary"></a>
					</div>
					<label class="control-label select2">Campos</label>	<br/>				
					<form:select data-live-search="true" class="selectpicker show-menu-arrow" path="fields" items="${fields}"> 		
		       		 </form:select>
		       		 
		       		 
		       		 	  
					<div class="form-actions"><br/>
						<button type="submit" class="btn btn-success">Submit</button>
						<button type="reset" class="btn">Cancel</button>
					</div>	
						
			</div>
			<c:if test="${fn:length(itemList) gt 0}">
				<div class="col-md-4 col2">
						<label class="control-label" >Resultado da query:</label><br/>
						
						<c:forEach var="item" items="${idList}">
						  <a href="${link}${item.id}" target="_blank">${item.id}</a>
						</c:forEach>
						<pre>${itemList}</pre>		
						
				</div>
			</c:if>
			
		</div>
	</div>
			
</form:form>

<div class="container">
  
  <h4>More Switch Examples</h4>
  <ul class="list-inline">
    <li>
      <div class="btn-group btn-toggle"> 
    	<button class="btn btn-xs btn-default">ON</button>
    	<button class="btn btn-xs btn-success active">OFF</button>
    	</div>
    </li>
    <li>
      <div class="btn-group btn-toggle"> 
    	<button class="btn btn-xs btn-default">ON</button>
    	<button class="btn btn-xs btn-danger active">OFF</button>
    	</div>
    </li>
    <li>
      <div class="btn-group btn-toggle"> 
    	<button class="btn btn-xs btn-default">ON</button>
    	<button class="btn btn-xs btn-info active">OFF</button>
    	</div>
    </li>
    <li>
      <div class="btn-group btn-toggle"> 
    	<button class="btn btn-xs btn-success">Y</button>
    	<button class="btn btn-xs btn-danger active">N</button>
    	</div>
    </li>
    <li>
      <div class="btn-group btn-toggle"> 
    	<button class="btn btn-xs btn-default">1</button>
    	<button class="btn btn-xs btn-primary active">0</button>
    	</div>
    </li>
    <li>
      <div class="btn-group btn-toggle"> 
        <button class="btn btn-xs btn-primary active">Preview</button>
    	<button class="btn btn-xs btn-default">Source Code</button>
    	</div>
    </li>
    <li>
      <div class="btn-group btn-toggle"> 
    	<button class="btn btn-xs btn-info">Yes</button>
    	<button class="btn btn-xs btn-primary active">No</button>
    	</div>
    </li>
  </ul>

  <script type="text/javascript">
  
  $("#e2").select2({
	    placeholder: "Select a State",
	    allowClear: true
	});
<!--  $('.btn-toggle').click(function() {
	    $(this).find('.btn').toggleClass('active');  
	    
	    if ($(this).find('.btn-primary').size()>0) {
	    	$(this).find('.btn').toggleClass('btn-primary');
	    }
	    if ($(this).find('.btn-danger').size()>0) {
	    	$(this).find('.btn').toggleClass('btn-danger');
	    }
	    if ($(this).find('.btn-success').size()>0) {
	    	$(this).find('.btn').toggleClass('btn-success');
	    }
	    if ($(this).find('.btn-info').size()>0) {
	    	$(this).find('.btn').toggleClass('btn-info');
	    }
	    
	   
	       
	});
-->
	$('form').submit(function(){
		alert($(this["options"]).val());
	    return false;
	});
  
  </script> 
    
</div>
  
</body>
</html>