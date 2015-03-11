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
	private int start = 0;
	private Integer aux;
	private int a = 0;
	private QueryForm queryForm = new QueryForm();
	private static Integer QUANTITY = 5; 
	private int brandStart;
	private Map<String,List<String>> kitGroup = new HashMap<String, List<String>>();
	private static final String FASHIONDEP = "10009073 10009074 10009075 10009076 10009077 10009078 10009079 10009069";
	

	@RequestMapping(value="/")
	public ModelAndView test() throws IOException{
			ModelAndView mav = new ModelAndView("home","query",new QueryForm());			
		return mav;
	}
	/*
	private Object getFields() {
		LinkedHashMap<String,String> fields = new LinkedHashMap<String, String>();
		for(Field dados : IndexedItem.class.getDeclaredFields()){
		 		 fields.put(dados.getName(),dados.getName());		
		}

		Set<String> keys = new TreeSet<String>();
		keys.addAll(fields.keySet());
		return keys;
	}

	@RequestMapping(value="/busca", method=RequestMethod.GET)
	public ModelAndView navegue(@ModelAttribute("query") QueryForm queryForm, QueryFormPartner queryFormPartner, Model model , BindingResult result) throws IOException, SolrServerException, JAXBException{
		if(result.hasFieldErrors()){
			 ModelAndView mv =  new ModelAndView("teste");
			 mv.addObject("msg","Ocorreu um erro");
			 return mv;
		}else{
			List<IndexedItem> listIndexedItem = new ArrayList<IndexedItem>();		
			String solrUrl = getSolrBrand(queryForm) ;
			ItemSolrDao itemSolrDao = getItemSolrDao(solrUrl);
			ModelAndView mv =  new ModelAndView("home");
			mv.addObject("fields",getFields());			
			listIndexedItem = getItem(itemSolrDao,solrUrl,queryForm,queryFormPartner);
			model.addAttribute("idList",listIndexedItem);
			model.addAttribute("link",getLink(queryForm.getBrand()));
			model.addAttribute("size",listIndexedItem.size());
			return mv;
		}
			
	}
	
*/

	@RequestMapping(value="/busca2", method=RequestMethod.GET)
	public ModelAndView naveguetemp(@ModelAttribute("query") QueryForm queryForm, QueryFormPartner queryFormPartner, Model model , BindingResult result) throws IOException, SolrServerException, JAXBException, ClassNotFoundException{
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
		ModelAndView mv =  new ModelAndView("resultado");
		long initExecutionTime = System.currentTimeMillis();
		listIndexedItem = getItem(itemSolrDao,solrUrl,queryForm,queryFormPartner);
		if(listIndexedItem.isEmpty()){
			model.addAttribute("msg","Nenhum item foi encontrado !");
		}else{
			model.addAttribute("idList",listIndexedItem);
			model.addAttribute("itemList",getItensById(itemSolrDao, getIdList(listIndexedItem),listIndexedItem.size()));
			model.addAttribute("link",getLink(queryForm.getBrand()));
			model.addAttribute("size",listIndexedItem.size());
			model.addAttribute("solrLink",getSolrBrand(queryForm)+"/idxItem/select?q=itemId%3A");
			model.addAttribute("kitGroups", getKitGroup());
		}
		log.warn("tempo total "+ (initExecutionTime - System.currentTimeMillis()));
		return mv;
		
//			Map<String, String> resultados = new HashMap<String, String>();
//			resultados.put("itemList", toJson(listIndexedItem,fieldsArray));
//			resultados.put("totalEncontrados", Integer.valueOf(listIndexedItem.size()).toString());
//			resultados.put("link", (String) getLink(queryForm.getBrand().toString()));
//			/*for (IndexedItem indexedItem : listIndexedItem){
//				resultados.put(indexedItem.getId(), indexedItem);
//			}*/
//			//System.out.println(brandSolr.getIpList());
//			return new ResponseEntity<QueryResult>(new QueryResult(listIndexedItem, getLink(queryForm.getBrand().toString())), HttpStatus.OK);
//		}
			
	}

	/*private String toJson(List<IndexedItem> listIndexedItem, String[] fieldsArray)
			throws IOException {
		Map<String,String> array= new HashMap<String, String>();
		for (IndexedItem indexedItem : listIndexedItem){			
		    array.put("itemId", indexedItem.getId());
		    array.put("name", indexedItem.getItemName());
			if(fieldsArray.length == 0 ){
				ObjectMapper mapper = new ObjectMapper();
			     mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			     mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
			     mapper.setSerializationInclusion(Include.NON_EMPTY);
			     array.put("itemJson",mapper.writeValueAsString(indexedItem));
			}else{
				JSONSerializer postDetailsSerializer = new JSONSerializer().include(fieldsArray).exclude("*").prettyPrint(true);
				array.put("itemJson",postDetailsSerializer.serialize(indexedItem));
			}
			
		}
		return array.toString();
	}
	*/

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
			brandStart=20000;
			return "http://10.13.147.14:8080/solr" ;
		}else if(queryForm.getBrand().equals("americanas")){
			brandStart=50000;
			return "http://10.13.51.14:8080/solr";
		}else if(queryForm.getBrand().equals("shoptime")){
			brandStart=40000;
			return "http://10.13.67.14:8080/solr";
		}else if(queryForm.getBrand().equals("soubarato")){
			brandStart=500;
			return "http://10.13.91.21:8080/solr";
		}else{
			brandStart=50;
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
	
	
	
	@SuppressWarnings("unchecked")
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
			return getItens(itemSolrDao, queryString,QUANTITY,getRandom(brandStart));
			
		}else {
			Integer numPartner = getNumPartner();	
			String stock = queryForm.getStock();
			queryString.append(getQueryType(type));	
			queryString.append(" AND -erpDepartamentId:("+ FASHIONDEP+")");
			/*if(stock.equals("true"))
				queryString.append(" AND itemStockQuantity:[1 TO *]");
			else
				queryString.append(" AND itemStockQuantity:[0 TO 0]");*/
			queryString.append(" AND partnerList:[1 TO *]");
			List<IndexedItem> listIndexedItems = new ArrayList<IndexedItem>();
	    	while(listIndexedItems.size() < QUANTITY){
	    		String fields = "itemId,isMarketPlace,isExclusiveMarketPlace,partnerList,skuList";
	    		List<IndexedItem> indexedItemList = getSimpleItens(itemSolrDao, queryString, 500,getIncrement(Integer.valueOf(Integer.valueOf(getRandom(brandStart)))),fields);
				//String stockPartner = queryFormPartner.getStockPartner();
					//MarketPlaceSolrDao marketPlaceDao = getMarketPlaceItemSolrDao(solrUrl);
					if(indexedItemList != null &&  indexedItemList.size() > 0){
						for(IndexedItem indexedItem : indexedItemList){
							/*StringBuffer queryStringPartner= new StringBuffer();
							queryStringPartner.append("itemId:"+indexedItem.getId());
							SolrQuery queryPartner = new SolrQuery(queryStringPartner.toString());
							List<IndexedMarketPlaceItem> indexedMarketPlaceItems = marketPlaceDao.query(queryPartner);
							if(indexedMarketPlaceItems !=null && indexedMarketPlaceItems.size() > 0 ){
								for(IndexedMarketPlaceItem indexedMarketPlaceItem : indexedMarketPlaceItems){
									if(indexedMarketPlaceItem.getItemStock() == Boolean.valueOf(stockPartner) &&
											indexedMarketPlaceItem.getPartnerStatus() == Boolean.valueOf(stockPartner) &&*/ 
												if(indexedItem.getPartnerList() != null && indexedItem.getPartnerList().size() == numPartner && 
													indexedItem.getSkuList().size() ==  Integer.valueOf(queryForm.getNumSkus().isEmpty() ? "1" : queryForm.getNumSkus())){
										System.out.println("Quant" +indexedItem.getItemStockQuantity() + "skusize"+ indexedItem.getSkuList().size()  );
										listIndexedItems.add(indexedItem);
										if(listIndexedItems.size() >= QUANTITY)
											return listIndexedItems;
										//break;
									}/*else{
										return listIndexedItems;
									}*/
								
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
			log.warn("start "+ start);
			query.addField("itemId,isMarketPlace,itemStock");
			query.addFilterQuery("+(+isFreeBee:false -soldSeparatelly:false -item_property_EXCLUSIVE_B2B:true)");
			try{
				listIndexedItems = itemSolrDao.query(query);
				log.warn("TotalResults "+ itemSolrDao.getTotalResults());
			}catch(Exception e){
				log.warn("Ocorreu exceção "+ e.getMessage());
			}
		}
		return listIndexedItems;
	}
	
	@SuppressWarnings("unchecked")
	private List<IndexedItem> getItensB2wBySku(ItemSolrDao itemSolrDao, StringBuffer queryString, String start2,Integer skuQuantity) {
		//aux=Integer.valueOf(start2);
		String fields = "itemId,isMarketPlace,itemStock,skuList";
		List<IndexedItem> listIndexedItemsBysku = new ArrayList<IndexedItem>();
		while(listIndexedItemsBysku.size() < QUANTITY){			
			List<IndexedItem> indexedItemList = getSimpleItens(itemSolrDao, queryString, 500,getIncrement(Integer.valueOf(start2)),fields);
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
		}
		return listIndexedItemsBysku;
	}



	private List<IndexedItem> getFashionList(ItemSolrDao itemSolrDao,StringBuffer queryString, String numSkus, String type) throws SolrServerException {
		Integer skuQty = StringUtils.isEmpty(numSkus) ? 8 : Integer.valueOf(numSkus);
		SolrQuery query = new SolrQuery(queryString.toString());
		query.add("rows",String.valueOf(QUANTITY));
		String fields = "itemId,itemName,itemStock,skuList,erpDepartamentId,isMarketPlace,partnerList";
		query.addField(fields);
		Integer numPartner = getNumPartner();
		List<IndexedItem> listIndexedItemsFashion = new ArrayList<IndexedItem>();
		if(type.equals("b2w")){
			return getItens(itemSolrDao, queryString,QUANTITY,getRandom(brandStart));
		}
		while(listIndexedItemsFashion.size() < QUANTITY){
				List<IndexedItem> listIndexedItems = getSimpleItens(itemSolrDao, queryString, 500,getIncrement(Integer.valueOf(getRandom(brandStart))),fields);
				System.out.println("Moda"+itemSolrDao.getTotalResults());
				if(itemSolrDao.getTotalResults() <= aux ){
					listIndexedItems = getSimpleItens(itemSolrDao, queryString, 500,getIncrement(Integer.valueOf(getRandom((int) (itemSolrDao.getTotalResults())))),fields);
				}
				for(IndexedItem indexedItem : listIndexedItems){
					if(StringUtils.isEmpty(numSkus) && StringUtils.isEmpty(queryForm.getNumPartner()) && indexedItem.getPartnerList() != null && indexedItem.getPartnerList().size() >= 1){
						listIndexedItemsFashion.add(indexedItem);
					}else if(!StringUtils.isEmpty(numSkus) && StringUtils.isEmpty(queryForm.getNumPartner()) && indexedItem.getSkuList().size() == Integer.valueOf(skuQty)){
						listIndexedItemsFashion.add(indexedItem);
					}else if(StringUtils.isEmpty(numSkus) && !StringUtils.isEmpty(queryForm.getNumPartner()) && indexedItem.getPartnerList() != null && indexedItem.getPartnerList().size() == Integer.valueOf(numPartner)){
						listIndexedItemsFashion.add(indexedItem);
					}else if(indexedItem.getSkuList().size() == Integer.valueOf(skuQty) && indexedItem.getPartnerList() != null && indexedItem.getPartnerList().size() == Integer.valueOf(numPartner)){
						listIndexedItemsFashion.add(indexedItem);
					}
										
					if(listIndexedItemsFashion.size() == QUANTITY){							
						return listIndexedItemsFashion;
					}
				}
		}
		return null;
	}
	

	private Integer getNumPartner() {
		return Integer.valueOf(StringUtils.isEmpty(queryForm.getNumPartner()) ? "1" : queryForm.getNumPartner());
	}
	
	/*private String getStart2() {
		return StringUtils.isEmpty(queryForm.getStart()) ? "0" : queryForm.getStart();
	}*/

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
		//aux=Integer.valueOf(getRandom(brandStart));
		while(listIndexedItemsKit.size() < QUANTITY){
			SolrQuery query = new SolrQuery(queryString.toString());
			query.add("rows",String.valueOf(QUANTITY));
			//query.add("start",getStart());
			List<IndexedItem> listIndexedItems = getSimpleItens(itemSolrDao, queryString, 500,getIncrement(Integer.valueOf(getRandom(brandStart))),fields);
			System.out.println("kit"+itemSolrDao.getTotalResults());
			if(itemSolrDao.getTotalResults() <= aux ){
				listIndexedItems = getSimpleItens(itemSolrDao, queryString, 500,getRandom((int) (itemSolrDao.getTotalResults())),fields);
			}
			/*if(itemSolrDao.getTotalResults() < aux ){
				return listIndexedItemsKit;			
			}*/
			for(IndexedItem indexedItem : listIndexedItems){
				if(skuQty > 1){
					if(indexedItem.getSkuList().size() == skuQty){
						listIndexedItemsKit.add(indexedItem);
						if(listIndexedItemsKit.size() == QUANTITY){							
							return listIndexedItemsKit;
						}
					}
				}else{					
					listIndexedItemsKit.add(indexedItem);
					if(listIndexedItemsKit.size() == QUANTITY){
						kitGroup = getKitGroupList(listIndexedItemsKit);
						return listIndexedItemsKit;
					}
					
				}
			}
		}
		return null;
	}

	/*private List<IndexedItem> getItensBySkuQuantity(ItemSolrDao itemSolrDao,StringBuffer queryString, int skuQuantity, String[] fieldsArray) {
		List<IndexedItem> listIndexedItemsBysku = new ArrayList<IndexedItem>();
		while(listIndexedItemsBysku.size() < 2){
			List<IndexedItem> listIndexedItems = getItens(itemSolrDao, queryString, QUANTITY,getStart(),fieldsArray);
			for(IndexedItem indexedItem : listIndexedItems){
				if(indexedItem.getSkuList().size() == skuQuantity){
					listIndexedItemsBysku.add(indexedItem);
					if(listIndexedItemsBysku.size() == 2){
						return listIndexedItemsBysku;
					}
				}
				
			}
		}
		return listIndexedItemsBysku;
		if (getSkuKitInfoList() != null) {
			for (String skuKitInfo : getSkuKitInfoList()) {
				String kitInfos[] = skuKitInfo.split("\\^");
				if (skuId.equalsIgnoreCase(kitInfos[0].trim()))
	}*/

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

	private String getStart() {
		if(StringUtils.isEmpty(queryForm.getStart())){			
			if(a == 0 ){
				a = start;
				a =+ 500;
				return String.valueOf(start);
			}else{
				start =  a;
				a = a + 500;
				return String.valueOf(start);
			}
		}else{
			return queryForm.getStart();
		}
		
	}

	private List<IndexedItem> getItensById(ItemSolrDao itemSolrDao,
			StringBuffer queryString, int rows) {
		SolrQuery query = new SolrQuery(queryString.toString());	
		query.add("rows",String.valueOf(rows));
		List<IndexedItem> listIndexedItems = new ArrayList<IndexedItem>();
		try{
			listIndexedItems = itemSolrDao.query(query);
			log.warn("TotalResults "+ itemSolrDao.getTotalResults());
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
		query.add("rows",String.valueOf(quantity));
		query.add("start",start2);
		query.addFilterQuery("+(+isFreeBee:false -soldSeparatelly:false -item_property_EXCLUSIVE_B2B:true)");
		query.addField(fields);
		log.warn("Start " +start2);		
		try{
			indexedItemList = itemSolrDao.query(query);
			log.warn("TotalResults "+ itemSolrDao.getTotalResults());
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

	public void setBrandStart(int brandStart) {
		this.brandStart = brandStart;
	}

	public Map<String, List<String>> getKitGroup() {
		return kitGroup;
	}

	public void setKitGroup(Map<String, List<String>> kitGroup) {
		this.kitGroup = kitGroup;
	}
	
	
	
}
