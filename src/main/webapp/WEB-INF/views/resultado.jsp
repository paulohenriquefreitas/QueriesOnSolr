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
<style type="text/css">
.corner {
   float:right;
}	
</style>	
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
				                <ul class="list-unstyled">				                  
								  <li><span class="text-danger">ItemStock :</span> ${item.inStock}</li>
								  <li><span class="text-danger">ItemStockQuantity :</span> ${item.itemStockQuantity}</li>
								  <li><span class="text-danger">ItemStockQuantityNew :</span> ${item.itemStockQuantityNew}</li>
								  <li><span class="text-danger">ItemStockQuantityRewrapped :</span> ${item.itemStockQuantityRewrapped}</li>
								  <c:if test="${item.defaultPrice ne null}">
								  <li><span class="text-danger">DefaultPrice :</span> ${item.defaultPrice}</li>
								  </c:if>
								  <li><span class="text-danger">SalesPrice :</span> ${item.salesPrice}</li>
								  <li><span class="text-danger">AdminTagList :</span> ${item.adminTagList}</li>
								  <li><span class="text-danger">SiteStructure :</span> ${item.siteStructure}</li>
								  <c:if test="${item.isMarketPlace}">
								  <li><span class="text-danger">MainSellerId :</span> ${item.mainSellerId}</li>
								  <li><span class="text-danger">MainSellerName :</span> ${item.mainSellerName}</li>
								  <li><span class="text-danger">PartnerList :</span> ${item.partnerList}</li>
								  <li><span class="text-danger">PartnerListId :</span> ${item.partnerListIdt}</li>
								  <li><span class="text-danger">PartnerSmallestDefaultPrice :</span> ${item.partnerSmallestDefaultPrice}</li>
								  <li><span class="text-danger">PartnerSmallestSalesPrice :</span> ${item.partnerSmallestSalesPrice}</li>
								  </c:if>
								  <li><span class="text-danger">FlatGroupId :</span> ${item.flatGroupsIds}</li>
								  <li><span class="text-danger">SkuStock :</span> ${item.skuStock}</li>
								  <li><span class="text-danger">SkuStockQuantity :</span> ${item.skuStockQuantity}</li>
								  
								  <li><span class="text-danger">SkuList :</span> ${item.skuList}</li>
								  <li><span class="text-danger">ImageSkuUrlList :</span> ${item.imageSkuUrlList}</li>
								  <c:if test="${item.isKit}">
								  <li><span class="text-danger">IsKit :</span> ${item.isKit	}</li>
								  <li><span class="text-danger">SkuKitList :</span> ${item.skuKitList}</li>
								  <li><span class="text-danger">Filhos do Kit :</span> ${item.kitItemList}</li>
								  </c:if>
								  <li><span class="text-danger">IsMarketPlace :</span> ${item.isMarketPlace}</li>
								  <li><span class="text-danger">IsExclusiveMarketPlace :</span> ${item.isExclusiveMarketPlace}</li>
								   <c:if test="${item.numReviews gt 0}">
								  <li><span class="text-danger">AverageOverallRating :</span> ${item.averageOverallRating}</li>
								  <li><span class="text-danger">NumReviews :</span> ${item.numReviews}</li>
								  </c:if>
								       	
								</ul>
								<div class="corner">
				                	<span class="text-primary"> ... veja + em 
					                    <a href="${solrLink}${item.id}&wt=json&indent=true" target="_blank">json</a> ou em	
										<a href="${solrLink}${item.id}&wt=xml&indent=true" target="_blank"> xml</a>
									</span>	
								</div>
				                </div>
				            </div>
				        </div> 
			       </c:forEach> 
			    </div>			
		</div>
	 </c:if>	 
</body>
</html>