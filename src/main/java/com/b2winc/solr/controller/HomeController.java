package com.b2winc.solr.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.ideais.metasolr.template.CommonSolrTemplate;

import com.b2w.catalogbackendcommons.index.IndexedItem;
import com.b2w.catalogbackendcommons.index.IndexedMarketPlaceItem;
import com.b2winc.solr.model.BrandSolr;
import com.b2winc.solr.model.QueryForm;
import com.b2winc.solr.model.QueryFormPartner;
import com.b2winc.solr.repository.ItemSolrDao;
import com.b2winc.solr.repository.MarketPlaceSolrDao;


@Controller
public class HomeController {
	private static final Log log = LogFactory.getLog(HomeController.class);
	private BrandSolr brandSolr;
	private Integer aux;
	private QueryForm queryForm = new QueryForm();
	private static Integer QUANTITY = 5; 
	private Map<String,List<String>> kitGroup = new HashMap<String, List<String>>();
	private static final String FASHIONDEP = "10009073 10009074 10009075 10009076 10009077 10009078 10009079 10009069";
	

	@RequestMapping(value="/")
	public ModelAndView test() throws IOException{
			ModelAndView mav = new ModelAndView("home","query",new QueryForm());			
		return mav;
	}

	@RequestMapping(value="/busca", method=RequestMethod.GET)
	public ModelAndView navegue(@ModelAttribute("query") QueryForm queryForm, QueryFormPartner queryFormPartner, Model model , BindingResult result) throws IOException, SolrServerException, JAXBException, ClassNotFoundException{
		aux=null;
	    this.queryForm = queryForm;
		this.queryForm.setNumSkus(queryForm.getNumSkus());	    
		if(StringUtils.isNotEmpty(queryForm.getRows())){
	    	QUANTITY = Integer.valueOf(queryForm.getRows());
	    }else{
	    	QUANTITY = 5;
	    }
	    	
		List<IndexedItem> listIndexedItem = new ArrayList<IndexedItem>();
		String solrUrl = getSolrBrand(queryForm) ;
		ItemSolrDao itemSolrDao = getItemSolrDao(solrUrl);
		MarketPlaceSolrDao marketPlaceSolrDao = getMarketPlaceItemSolrDao(solrUrl);
		ModelAndView mv =  new ModelAndView("resultado");
		long initExecutionTime = System.currentTimeMillis();
		listIndexedItem = getItem(itemSolrDao,solrUrl,queryForm,queryFormPartner);
		if(listIndexedItem.isEmpty()){
			model.addAttribute("msg","Nenhum item foi encontrado !");
		}else{
			model.addAttribute("idList",listIndexedItem);
			model.addAttribute("itemList",getItensById(itemSolrDao, getIdList(listIndexedItem),listIndexedItem.size()));
			model.addAttribute("link",getLink(queryForm.getBrand()));
			model.addAttribute("linkWebStore",getLinkWebstore(queryForm.getBrand()));
			model.addAttribute("size",listIndexedItem.size());
			model.addAttribute("solrLink",getSolrBrand(queryForm)+"/idxItem/select?q=itemId%3A");
			model.addAttribute("solrLinkPartner",getSolrBrand(queryForm)+"/idxMarketPlace/select?q=itemId%3A");
			model.addAttribute("kitGroups", getKitGroup());
			model.addAttribute("partnersMap", getParnerList(marketPlaceSolrDao,listIndexedItem));
		}
		log.info("Tempo total:  "+ (initExecutionTime - System.currentTimeMillis()));
		return mv;			
	}


	private Map<String, List<IndexedMarketPlaceItem>> getParnerList(MarketPlaceSolrDao marketPlaceSolrDao, List<IndexedItem> listIndexedItem) {
		Map<String , List<IndexedMarketPlaceItem>> partnerMap = new HashMap<String, List<IndexedMarketPlaceItem>>() ;
		if(!listIndexedItem.isEmpty()){
			for (IndexedItem indexedItem : listIndexedItem) {
				if(indexedItem.getPartnerList() !=null && indexedItem.getPartnerList().size() > 0){
					List<IndexedMarketPlaceItem> partners = new ArrayList<IndexedMarketPlaceItem>();
					StringBuffer queryString= new StringBuffer();
					queryString.append("itemId:"+indexedItem.getId());
					partners = marketPlaceSolrDao.query(queryString.toString());
						partnerMap.put(indexedItem.getId(), partners);
				}
			}
		}
		return partnerMap;
	}

	private StringBuffer getIdList(List<IndexedItem> listIndexedItem) {
		StringBuffer idList = new StringBuffer();
		idList.append("itemId:(");
		for (IndexedItem indexedItem : listIndexedItem) {
			idList.append(indexedItem.getId()+" ");
		}
		idList.append(")");
		return idList;
	}

	private String getSolrBrand(QueryForm queryForm) {
		if(queryForm.getBrand().equals("submarino")){
			return "http://10.13.147.14:8080/solr" ;
		}else if(queryForm.getBrand().equals("americanas")){
			return "http://10.13.51.14:8080/solr";
		}else if(queryForm.getBrand().equals("shoptime")){
			return "http://10.13.67.14:8080/solr";
		}else if(queryForm.getBrand().equals("soubarato")){
			return "http://10.13.91.21:8080/solr";
		}else{
			return "http://vmhmlwl1:8080/solr";
		}
	}

	
	public static ItemSolrDao getItemSolrDao(String solrUrl) throws MalformedURLException{
		CommonSolrTemplate solrTemplate =  new CommonSolrTemplate();
		HttpSolrServer solrServer = new HttpSolrServer(solrUrl+"/idxItem");
		solrTemplate.setServer(solrServer);
		ItemSolrDao itemSolrDao = new ItemSolrDao();
		itemSolrDao.setTemplate(solrTemplate);
		
		return itemSolrDao;
	}
	
	public static MarketPlaceSolrDao getMarketPlaceItemSolrDao(String solrUrl) throws MalformedURLException{
		CommonSolrTemplate solrTemplate =  new CommonSolrTemplate();
		HttpSolrServer solrServer = new HttpSolrServer(solrUrl+"/idxMarketPlace");
		solrTemplate.setServer(solrServer);
		MarketPlaceSolrDao itemSolrDao = new MarketPlaceSolrDao();
		itemSolrDao.setTemplate(solrTemplate);
		
		return itemSolrDao;
	}
	
	
	
	private List<IndexedItem> getItem(ItemSolrDao itemSolrDao,String solrUrl, QueryForm queryForm, QueryFormPartner queryFormPartner) throws SolrServerException, IOException{
		StringBuffer queryString= new StringBuffer();
		if(StringUtils.isNotEmpty(System.getProperty("query")))
			queryString.append(System.getProperty("query"));
		String id = queryForm.getId();
		String type = queryForm.getType();
		if(StringUtils.isNotEmpty(id) ) {
			queryString.append("itemId:("+id+")");
			return getItensById(itemSolrDao, queryString,10);
		}else if(queryForm.getFashion().equalsIgnoreCase("true")){
			queryString.append("itemStock:"+queryForm.getStock());	
			queryString.append(" AND erpDepartamentId:("+ FASHIONDEP+")");
			queryString.append(" AND "+getQueryType(queryForm.getType()));
			queryString.append(" AND "+"isKit:"+queryForm.getKit());
			return getFashionList(itemSolrDao, queryString,queryForm.getNumSkus(),type);
		}else if(queryForm.getKit().equalsIgnoreCase("true")){
			String stock = queryForm.getStock();						
			queryString.append("itemStock:"+stock);	
			queryString.append(" AND isKit:true");
			return getKits(itemSolrDao, queryString,queryForm.getNumSkus());
		}else if (type.equals("b2w")){			
			queryString.append(getQueryType(type));
			String stock = queryForm.getStock();						
			queryString.append(" AND itemStock:"+stock);
			queryString.append(" AND -erpDepartamentId:("+ FASHIONDEP+")");
			@SuppressWarnings("unused")
			List<IndexedItem> indexedItemListCounter = getSimpleItens(itemSolrDao, queryString, 1,"1","itemId");
			int totalResult = (int) itemSolrDao.getTotalResults();
			int random = Integer.valueOf(getRandom(totalResult));
			return getItens(itemSolrDao, queryString,QUANTITY,getIncrement(random));
			
		}else {
			Integer numPartner = getNumPartner();	
			queryString.append(getQueryType(type));	
			queryString.append(" AND -erpDepartamentId:("+ FASHIONDEP+")");			
			queryString.append(" AND partnerList:[1 TO *]"); 
			List<IndexedItem> listIndexedItems = new ArrayList<IndexedItem>();
			String fields = "itemId,isMarketPlace,isExclusiveMarketPlace,partnerList,skuList";
			@SuppressWarnings("unused")
			List<IndexedItem> indexedItemListCounter = getSimpleItens(itemSolrDao, queryString, 1,"1","itemId");
			int totalResult = (int) itemSolrDao.getTotalResults();
			int random = Integer.valueOf(getRandom(totalResult));
	    	while(listIndexedItems.size() < QUANTITY){
	    		List<IndexedItem> indexedItemList = getSimpleItens(itemSolrDao, queryString, 500,getIncrement(random),fields);
					if(indexedItemList != null &&  indexedItemList.size() > 0){
						for(IndexedItem indexedItem : indexedItemList){
							if(indexedItem.getPartnerList() != null && indexedItem.getPartnerList().size() == numPartner && 
													indexedItem.getSkuList().size() ==  Integer.valueOf(queryForm.getNumSkus().isEmpty() ? "1" : queryForm.getNumSkus())){
								listIndexedItems.add(indexedItem);
								if(listIndexedItems.size() >= QUANTITY)
									return listIndexedItems;
										
							}
						}
					}else{
						return listIndexedItems;
					}
			}
			
			return listIndexedItems;
		}
		
	}
	
	private List<IndexedItem> getItens(ItemSolrDao itemSolrDao,
			StringBuffer queryString,Integer quantity, String start) throws SolrServerException {
		List<IndexedItem> listIndexedItems = new ArrayList<IndexedItem>();
		SolrQuery query = new SolrQuery(queryString.toString());
		if(StringUtils.isNotEmpty(this.queryForm.getNumSkus())){
			listIndexedItems = getItensB2wBySku(itemSolrDao,queryString,start,Integer.valueOf(this.queryForm.getNumSkus()));
		}else{
			query.add("rows",String.valueOf(quantity));
			query.add("start",start);
			query.addField("itemId,isMarketPlace,itemStock");
			query.addFilterQuery("+(+isFreeBee:false -soldSeparatelly:false -item_property_EXCLUSIVE_B2B:true)");
			try{
				log.info("Start: " +start);
				listIndexedItems = itemSolrDao.query(query);
			}catch(Exception e){ 
				log.info("Ocorreu exceção "+ e.getMessage());
			}
		}
		return listIndexedItems;
	}
	
	private List<IndexedItem> getItensB2wBySku(ItemSolrDao itemSolrDao, StringBuffer queryString, String start2,Integer skuQuantity) {
		String fields = "itemId,isMarketPlace,itemStock,skuList";
		List<IndexedItem> listIndexedItemsBysku = new ArrayList<IndexedItem>();
		while(listIndexedItemsBysku.size() < QUANTITY){			
			List<IndexedItem> indexedItemList = getSimpleItens(itemSolrDao, queryString, 500,getIncrement(Integer.valueOf(start2)),fields);
			try{
				if(indexedItemList != null &&  indexedItemList.size() > 0){
					
					for(IndexedItem indexedItem : indexedItemList){
						if(indexedItem.getSkuList().size() == skuQuantity){
							listIndexedItemsBysku.add(indexedItem);
							if(listIndexedItemsBysku.size() == QUANTITY){
								return listIndexedItemsBysku;
							}
						}
						
					}
				}else{	
					return listIndexedItemsBysku;
				}
			}catch(Exception e){
				log.error("Ocorreu uma exceção. " + e.getMessage());
				return listIndexedItemsBysku;
			}
		}
		return listIndexedItemsBysku;
	}



	private List<IndexedItem> getFashionList(ItemSolrDao itemSolrDao,StringBuffer queryString, String numSkus, String type) throws SolrServerException {
		Integer skuQty = StringUtils.isEmpty(numSkus) ? 8 : Integer.valueOf(numSkus);
		SolrQuery query = new SolrQuery(queryString.toString());
		query.add("rows",String.valueOf(QUANTITY));
		String fields = "itemId,itemName,itemStock,skuList,erpDepartamentId,isMarketPlace,partnerList";
		query.addField(fields);
		List<IndexedItem> listIndexedItemsFashion = new ArrayList<IndexedItem>();
		@SuppressWarnings("unused")
		List<IndexedItem> indexedItemListCounter = getSimpleItens(itemSolrDao, queryString, 1,"1","itemId");
		int totalResult = (int) itemSolrDao.getTotalResults();
		int random = Integer.valueOf(getRandom(totalResult));
		
		while(listIndexedItemsFashion.size() < QUANTITY){
			List<IndexedItem> listIndexedItems = getSimpleItens(itemSolrDao, queryString, 500,getIncrement(random),fields);
			try{
				if(listIndexedItems != null &&  listIndexedItems.size() > 0){	
					for(IndexedItem indexedItem : listIndexedItems){
						if(type.equals("b2w") && StringUtils.isEmpty(numSkus)){
							listIndexedItemsFashion.add(indexedItem);
						}else if(type.equals("b2w") && !StringUtils.isEmpty(numSkus) && indexedItem.getSkuList().size() == Integer.valueOf(skuQty)){
							listIndexedItemsFashion.add(indexedItem);
						}else if(StringUtils.isEmpty(numSkus) && StringUtils.isEmpty(queryForm.getNumPartner()) && indexedItem.getPartnerList() != null && indexedItem.getPartnerList().size() >= 1){
							listIndexedItemsFashion.add(indexedItem);
						}else if(!StringUtils.isEmpty(numSkus) && StringUtils.isEmpty(queryForm.getNumPartner()) && indexedItem.getSkuList().size() == Integer.valueOf(skuQty)){
							listIndexedItemsFashion.add(indexedItem);
						}else if(StringUtils.isEmpty(numSkus) && !StringUtils.isEmpty(queryForm.getNumPartner()) && indexedItem.getPartnerList() != null && indexedItem.getPartnerList().size() == Integer.valueOf(queryForm.getNumPartner())){
							listIndexedItemsFashion.add(indexedItem);
						}else if(indexedItem.getSkuList().size() == Integer.valueOf(skuQty) && indexedItem.getPartnerList() != null && indexedItem.getPartnerList().size() == Integer.valueOf(queryForm.getNumPartner())){
							listIndexedItemsFashion.add(indexedItem);
						}
											
						if(listIndexedItemsFashion.size() == QUANTITY){							
							return listIndexedItemsFashion;
						}
					}
				}else{
					return listIndexedItemsFashion;
				}
			}catch(Exception e){
				log.error("Ocorreu uma exceção. " + e.getMessage());
				return listIndexedItemsFashion;
			}
		}
			
		return null;
	}
	

	private Integer getNumPartner() {
		return Integer.valueOf(StringUtils.isEmpty(queryForm.getNumPartner()) ? "1" : queryForm.getNumPartner());
	}

	private String getRandom(int brandStart) {
		if(StringUtils.isEmpty(queryForm.getStart())){
			return String.valueOf(1 + (int)(Math.random() * brandStart));
		}else{
			return queryForm.getStart();
		}
	}
	

	private List<IndexedItem> getKits(ItemSolrDao itemSolrDao,
			StringBuffer queryString,String skuQuantity) {
		Integer skuQty = StringUtils.isEmpty(skuQuantity) ? 1 : Integer.valueOf(skuQuantity);
		List<IndexedItem> listIndexedItemsKit = new ArrayList<IndexedItem>();
		String fields = "itemId,itemName,isKit,skuList,kitItemList";
		@SuppressWarnings("unused")
		List<IndexedItem> indexedItemListCounter = getSimpleItens(itemSolrDao, queryString, 1,"1","itemId");
		int totalResult = (int) itemSolrDao.getTotalResults();
		int random = Integer.valueOf(getRandom(totalResult));
		while(listIndexedItemsKit.size() < QUANTITY){
			SolrQuery query = new SolrQuery(queryString.toString());
			query.add("rows",String.valueOf(QUANTITY));
			List<IndexedItem> listIndexedItems = getSimpleItens(itemSolrDao, queryString, 500,getIncrement(random),fields);
			try{
				if(listIndexedItems != null &&  listIndexedItems.size() > 0){
					for(IndexedItem indexedItem : listIndexedItems){										
						listIndexedItemsKit.add(indexedItem);
						if(listIndexedItemsKit.size() == QUANTITY){
							kitGroup = getKitGroupList(listIndexedItemsKit);
							return listIndexedItemsKit;
						}	
					}
				}else{
					return listIndexedItems;
				}
			}catch(Exception e){
				log.error("Ocorreu uma exceção. " + e.getMessage());
				return listIndexedItemsKit;
			}
		}
		return null;
	}

	
	private Map<String, List<String>> getKitGroupList(List<IndexedItem> listIndexedItemsKit) {
		
		for (IndexedItem indexedItem : listIndexedItemsKit) {
			List<String> memberKits = new ArrayList<String>();
			if(indexedItem.getKitItemList() != null){
				for(String kitItem : indexedItem.getKitItemList()){
					String kitInfos[] = kitItem.split("\\^");
					if(!kitInfos[2].trim().isEmpty() && !memberKits.contains(kitInfos[2].trim())){
						memberKits.add(kitInfos[2].trim());						
					}
				}
				kitGroup.put(indexedItem.getId(), memberKits);	
			}
		}		
		return kitGroup;
	}


	private List<IndexedItem> getItensById(ItemSolrDao itemSolrDao,
			StringBuffer queryString, int rows) {
		SolrQuery query = new SolrQuery(queryString.toString());	
		query.add("rows",String.valueOf(rows));
		List<IndexedItem> listIndexedItems = new ArrayList<IndexedItem>();
		try{
			listIndexedItems = itemSolrDao.query(query);
		}catch (Exception e) {
			log.error("Ocorreu uma exceção. " + e.getMessage());
			return listIndexedItems;
		}	
		return listIndexedItems;
	}	
	

	private String getIncrement(Integer start2) {
		if(aux == null){
			aux = start2;
			return String.valueOf(start2);
		}else{
			aux=aux+500;
			
			return String.valueOf(aux);	
		}
	}

	private List<IndexedItem> getSimpleItens(ItemSolrDao itemSolrDao,StringBuffer queryString, int quantity, String start2, String fields) {
		SolrQuery query = new SolrQuery(queryString.toString());
		List<IndexedItem> indexedItemList = null;
		//query.add("rows",String.valueOf(quantity));
		query.add("start",start2);
		query.addFilterQuery("+(+isFreeBee:false -soldSeparatelly:false -item_property_EXCLUSIVE_B2B:true)");
		query.addField(fields);
		log.info("Start: " +start2);		
		try{
			indexedItemList = itemSolrDao.query(query);
			log.info("TotalResults "+ itemSolrDao.getTotalResults());
		}catch(Exception e){
			log.error("Ocorreu uma exceção. " + e.getMessage());
			return indexedItemList;
		}
		return  indexedItemList;
		
		
	}

	private String getQueryType(String type) {
		if(type!=null){
			if(type.equalsIgnoreCase("100"))
				return("isExclusiveMarketPlace:true");
			else if(type.equalsIgnoreCase("misto") && queryForm.getStock().equals("true"))
				return("isMarketPlace:true AND isExclusiveMarketPlace:false");
			else if(type.equalsIgnoreCase("misto") && queryForm.getStock().equals("false"))
				return("isMarketPlace:true AND isExclusiveMarketPlace:true");
			else
				return("isMarketPlace:false");
		}
		return "";
	}
	
	private String getLink(String marca) {
		if(marca.equalsIgnoreCase("homolog"))
			return "http://hml.www.americanas.com.br/produto/";
		return "http://www."+marca+".com.br/produto/";
	}
	
	private String getLinkWebstore(String marca) {
		if(marca.equalsIgnoreCase("homolog"))
			return "http://hml.www.americanas.com.br/lojista/";
		return "http://www."+marca+".com.br/lojista/";
	}	
	
	public BrandSolr getBrandSolr() {
		return brandSolr;
	}

	public void setBrandSolr(BrandSolr brandSolr) {
		this.brandSolr = brandSolr;
	}
	
	
	public QueryForm getQueryForm() {
		return queryForm;
	}

	public void setQueryForm(QueryForm queryForm) {
		this.queryForm = queryForm;
	}

	public Map<String, List<String>> getKitGroup() {
		return kitGroup;
	}

	public void setKitGroup(Map<String, List<String>> kitGroup) {
		this.kitGroup = kitGroup;
	}
	
}
