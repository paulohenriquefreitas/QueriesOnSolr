<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" href="/resources/css/bootstrap.css" rel="stylesheet"/>
<link type="text/css" href="/resources/css/hover.css" rel="stylesheet"/>
<script src="http://code.jquery.com/jquery.min.js"></script>
<script src="/resources/js/bootstrap.min.js"></script>
<title></title>
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
<body>
	<div class="container-fluid" >
		<div class="row-fluid">
			<div class="span12">
				<div style="background-color: #D2B48C;border-radius: 6px; height: 36px "  > 
					<ul class="nav nav-pills nav-justified" style="font-size:18px ">
						<li class="pulse-grow"><a  href="#tab1"><span class="label label-primary">SUBMARINO</span></a></li>
						<li class="pulse-grow"><a  href="#tab1"><span class="label label-primary">STAGING SUBA </span></a></li>
						<li class="pulse-grow"><a  href="#tab2"><span class="label label-danger" >AMERICANAS        </span></a></li>
						<li class="pulse-grow"><a  href="#tab2"><span class="label label-danger" >STAGING ACOM</span></a></li>
						<li class="pulse-grow"><a  href="#tab3"><span class="label label-warning">SHOPTIME</span></a></li>
						<li class="pulse-grow"><a  href="#tab3"><span class="label label-info"   >SOUBARATO</span></a></li>
						<li class="pulse-grow"><a  href="#tab3"><span class="label label-success">HOMOLOGAÇÃO</span></a></li>						
					</ul>
			   </div> 
          </div>			
		</div>
	</div>
	
	<div class="container">
		<div class="row clearfix">
			<div class="col-md-12 column">
				<h3 class="text-center">
					QueriesOnSolr
				</h3> 	
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
</body>
</html>