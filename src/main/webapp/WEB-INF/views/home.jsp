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
<link rel="stylesheet" type="text/css" href="/resources/css/jquery.tokenize.css" />

<link href="bootstrap-switch.css" rel="stylesheet">
<script src="http://code.jquery.com/jquery.min.js"></script>
<script src="/resources/js/bootstrap.min.js"></script>
<script src="/resources/js/bootstrap-select.js"></script>
<script src="/resources/js/select2.js"></script>
<script type="text/javascript" src="/resources/js/jquery.tokenize.js"></script>
<script src="/resources/js/heartcode-canvasloader.js"></script>

<link rel="stylesheet" href="/resources/css/style.css">
<link rel="stylesheet" href="/resources/css/prism.css">
<link rel="stylesheet" href="/resources/css/chosen.css">


<script type="text/javascript">

$(document).ready( function() {
	  var form = $('#query');
	  var resultado = $('.resultadoBusca');
	  
	  resultado.empty();
	  form.submit( function(event) {
		  resultado.children().each(function() { $(this).remove(); });
		  $('#loading-indicator').show();
	    $.ajax( {
	      type: "GET",
	      url: form.attr( 'action' ),
	      data: form.serialize(),
	      success: function( response ) {
	    	resultado.html(response);
	    	$('#loading-indicator').hide();
	      }
	    } );
	    event.preventDefault();
	  } );

	} );


	
</script>

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
	padding-left: 0px;
	border-radius: 8px;
	border: 2px solid #e3e3e3;
	}
	
	.col2{
	margin-bottom: 0px;
	padding-bottom: 5px;
	padding-left: 7px;
	border-radius: 8px;
	width: 78%;
	border: 2px solid #e3e3e3;
	}
	
	.col-wrap{
	overflow: hidden; 
	}
	
	input.full-width {
	    box-sizing: border-box;
	    width: 100%;
	    height: 30px;
	}
	
	#mode-group .btn:not(.active) {
		opacity: 0.5;
	}
	
	#loading-indicator {
	   border: 100px 10px 100px 100px;
	   display: none;
	}
	.wrapper {
	   position:absolute;
	   top:50%;
	   left:60%;
	}

}
</style> 
</head>
<body >
	
		
<div class="container">
	<div class="row clearfix">
		<div class="col-md-12 column">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">
						<img alt="40x40" src="/resources/image/Java_Log2o.png" class="img-rounded" />
						<span>QueriesOnSolr</span>
					</h3>
				</div>
			</div>
		</div>
	</div>
</div>
					
<form:form class="form-horizontal"  action="/busca2" method="get" modelAttribute="query" >
	<div class="container">
		<div class="row clearfix">
			<div class="col-md-12 column">
			     <div id=mode-group class="btn-group btn-group-justified font-group " data-toggle="buttons">
			      <label class="btn btn-primary active" ><form:radiobutton path="brand" value="submarino"  checked="checked"/>SUBMARINO</label>			        
			      <label class="btn btn-danger "><form:radiobutton path="brand" value="americanas" />AMERICANAS</label>			        
			      <label class="btn btn-shop " ><form:radiobutton path="brand" value="shoptime"/>SHOPTIME</label>
			      <label class="btn btn-soub" ><form:radiobutton path="brand" value="soubarato"/>SOUBARATO</label>
			      <label class="btn btn-success" ><form:radiobutton path="brand" value="americanas" />HOMOLOGAÇÃO</label>
			    </div>
			</div>
		</div>
	</div>
	
	
	<div class="container">
		<div class="row clearfix">
			<div class="col-md-4 col">
			    <div class="panel-group" id="accordion">
			    	<div class="panel panel-default">
			    		 <div class="panel-heading">
			                <h5 class="panel-title link">
			                    <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">Busca de Itens</a>
			                </h5>
			            </div>
			            <div id="collapseOne" class="panel-collapse collapse in">
                			<div class="panel-body">
								<label class="control-label">Item Id</label>
								<div class="controls ">
									<%-- <form:input class="form-control" placeholder="Pesquisa por Id"  path="id"/> --%>
									<input type="text" name="id" id="id" class="form-control"  placeholder="Pesquisa por Id"  />
								</div>		
							</div>
						</div>		
					</div>
					<div class="panel panel-default">	
						<div class="panel-heading">
			                <h5 class="panel-title">
			                    <a data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">Itens Marketplace</a>
			                </h5>
			            </div>
			            <div id="collapseTwo" class="panel-collapse collapse">
               				 <div class="panel-body">
               				 	<label class="control-label">Números de Parceiros</label>
								<div class="controls ">
									<form:input class="form-control" placeholder="Quantidade de parceiros"  path="numPartner"/>
								</div>               				 	
								<label class="control-label" >Tipo:</label>
								<div class="switch-toggle switch-3 well">
								<input id="b2w" type="radio"  name="type" value="b2w" checked /><label for="b2w" onclick="">B2W</label>
								<input id="misto" type="radio"  name="type" value="misto"/><label for="misto" onclick="">MISTO</label>
								<input id="100" type="radio"  name="type" value="100"/><label for="100" onclick="">100%</label>
								<a class="btn btn-primary"></a>					
								</div>
								
								<label class="control-label" >Estoque do Item:</label>				
								
								<div class="switch-toggle well">
								  <input id="true" type="radio" name="stock" value="true" checked> <label for="true" onclick="">TRUE</label>
								  <input id="false" type="radio" name="stock" value="false" > <label for="false" onclick="">FALSE</label>
								  <a class="btn btn-primary"></a>
								</div> 
								
								<label class="control-label" >Estoque do Parceiro:</label>				
								
								<div class="switch-toggle well">
								  <input id="truePartner" type="radio" name="stockPartner" value="true" checked> <label for="truePartner" onclick="">TRUE</label>
								  <input id="falsePartner" type="radio" name="stockPartner" value="false" > <label for="falsePartner" onclick="">FALSE</label>
								  <a class="btn btn-primary"></a>
								</div> 
							</div>
						</div>		
					</div>
					<div class="panel panel-default">
						<div class="panel-heading">
			                <h5 class="panel-title">
			                    <a data-toggle="collapse" data-parent="#accordion" href="#collapseThree">Itens de Moda</a>
			                </h5>
			            </div>
	            		<div id="collapseThree" class="panel-collapse collapse">
	               			 <div class="panel-body">											
								<label class="control-label" >Moda:   </label>
								<div class="switch-toggle well">
								  <input id="fashionTrue" type="radio" name="fashion" value="true" > <label for="fashionTrue" onclick="">TRUE</label>
								  <input id="fashionFalse" type="radio" name="fashion" value="false" checked> <label for="fashionFalse" onclick="">FALSE</label>
								  <a class="btn btn-primary"></a>
								</div> 
								<br/>
							</div>
						</div>		
					</div>	
					<div class="panel panel-default">
						<div class="panel-heading">
			                <h5 class="panel-title">
			                    <a data-toggle="collapse" data-parent="#accordion" href="#collapseFour">Kit</a>
			                </h5>
			            </div>
	            		<div id="collapseFour" class="panel-collapse collapse">
	               			 <div class="panel-body">											
								<label class="control-label" >Kit:   </label>
								<div class="switch-toggle well">
								  <input id="kitTrue" type="radio" name="kit" value="true" > <label for="kitTrue" onclick="">TRUE</label>
								  <input id="kitFalse" type="radio" name="kit" value="false" checked> <label for="kitFalse" onclick="">FALSE</label>
								  <a class="btn btn-primary"></a>
								</div> 
								<br/>
							</div>
						</div>		
					</div>
					<br/><hr/>
					<div class="panel-body ">
						<form:input class="smallfield" placeholder="Nº Skus"  path="numSkus"/>
						<form:input class="smallfield" placeholder="Start"  path="start"/>
						<form:input class="smallfield" placeholder="Rows"  path="rows"/>
					</div>  	
					 <div class="side-by-side clearfix">
			         <form:select id="tokenize" data-live-search="true" placeholder="Digite o(s) campo(s) " 
			                     class="chosen-select" tabindex="8" path="fields" items="${fields}"> 		
				     </form:select>
			          
					  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js" type="text/javascript"></script>
					  <script src="/resources/js/chosen.jquery.js" type="text/javascript"></script>
					  <script src="/resources/js/prism.js" type="text/javascript" charset="utf-8"></script>
					  <script type="text/javascript">
					    var config = {
					      '.chosen-select'           : {},
					      '.chosen-select-deselect'  : {allow_single_deselect:true},
					      '.chosen-select-no-single' : {disable_search_threshold:10},
					      '.chosen-select-no-results': {no_results_text:'Oops, nothing found!'},
					      '.chosen-select-width'     : {width:"95%"}
					    }
					    for (var selector in config) {
					      $(selector).chosen(config[selector]);
					    }
					  </script>
									
				     </div>
					       		 	  
					<div class="form-actions"><br/>
						<button type="submit" class="btn btn-success">Submit</button>
						<button type="reset" class="btn">Cancel</button>
					</div>	
						
						
			 
	
					<c:if test="${fn:length(itemList) gt 0}">
						<div class="col-md-4 col2">
								<label class="control-label" >Resultado da query: ${size}</label><br/>
								
								<c:forEach var="item" items="${idList}">
								  <a href="${link}${item.id}" target="_blank">${item.id}</a>
								</c:forEach>
								
								<pre>${itemList}</pre>		
								
						</div>
					</c:if>
				</div>	
			</div>
			
			<div class="resultadoBusca col-md-4 col2">
				
			    Total Encontrados: <span class="totalEncontrados"></span>
			    
			    <div class="panel-group " id="accordion">
				  <div class="panel panel-default itemList">
				    <div class="panel-heading">
				      <h4 class="panel-title">
				       
				        <i class="pull-right glyphicon glyphicon-chevron-down accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
				        </i>
				      </h4>
				    </div>

				    
				  </div>	
				</div>
			    <span class="link"></span>
				
			</div>
			<div class="container" id="loading-indicator">
				<!-- Create a div which will be the canvasloader wrapper -->	
				<div id="canvasloader-container" class="wrapper"></div>
			</div>
			 <script type="text/javascript">
						var cl = new CanvasLoader('canvasloader-container');
						cl.setColor('#819e0b'); // default is '#000000'
						cl.setShape('spiral'); // default is 'oval'
						cl.setDiameter(137); // default is 40
						cl.setDensity(32); // default is 40
						cl.setRange(0.9); // default is 1.3
						cl.setFPS(15); // default is 24
						cl.show(); // Hidden by default
							
							// This bit is only for positioning - not necessary
							  var loaderObj = document.getElementById("canvasLoader");
					  		loaderObj.style.position = "absolute";
					  		loaderObj.style["top"] = cl.getDiameter() * -0.5 + "px";
					  		loaderObj.style["left"] = cl.getDiameter() * -0.5 + "px";
		   </script>
		</div>
	</div>			
</form:form>
</body>
</html>