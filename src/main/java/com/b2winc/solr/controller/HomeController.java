package com.b2winc.solr.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
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
import com.b2winc.solr.modeljson.QueryResult;
import com.b2winc.solr.repository.ItemSolrDao;
import com.b2winc.solr.repository.MarketPlaceSolrDao;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import flexjson.JSONSerializer;

@Controller
public class HomeController {
	private BrandSolr brandSolr;
	private int start = 0;
	private Integer aux;
	private int a = 0;
	private QueryForm queryForm = new QueryForm();
	private static Integer QUANTITY = 5; 
	private static final String SEPARATOR = "\\s\\^\\s";
	private int brandStart;
	private Map<String,String> kitGroup = new HashMap<String, String>();

	@RequestMapping(value="/")
	public ModelAndView test() throws IOException{
			ModelAndView mav = new ModelAndView("home","query",new QueryForm());		
			mav.addObject("fields",getFields());	
		return mav;
	}
	
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
			String[] fieldsArray = queryForm.getFields();
			listIndexedItem = getItem(itemSolrDao,solrUrl,queryForm,queryFormPartner,fieldsArray);
			model.addAttribute("idList",listIndexedItem);
			/*model.addAttribute("itemList",toJson(listIndexedItem,fieldsArray));*/
			model.addAttribute("link",getLink(queryForm.getBrand()));
			model.addAttribute("size",listIndexedItem.size());
			return mv;
		}
			
	}
	
	private Object removeNull(List<IndexedItem> listIndexedItem) throws ClassNotFoundException {
		List<IndexedItem> listItens = new ArrayList<IndexedItem>();
		for (IndexedItem indexedItem : listIndexedItem) {
			for (IndexedItem indexedItem1 : listItens) {
				Class cls = Class.forName("indexedItem");
				Field fieldlist[] = cls.getDeclaredFields();
				for (Field field : fieldlist) {
					System.out.println(field);
				}
			}
		}
		return listIndexedItem;
	}

	@RequestMapping(value="/busca2", method=RequestMethod.GET)
	public ModelAndView naveguetemp(@ModelAttribute("query") QueryForm queryForm, QueryFormPartner queryFormPartner, Model model , BindingResult result) throws IOException, SolrServerException, JAXBException, ClassNotFoundException{
		this.queryForm.setNumSkus(queryForm.getNumSkus());
//		if(result.hasFieldErrors()){
//			 ModelAndView mv =  new ModelAndView("teste");
//			 mv.addObject("msg","Ocorreu um erro");
//			 return mv;
//		}else{
		    this.queryForm = queryForm;
		    if(StringUtils.isNotEmpty(queryForm.getRows())){
		    	QUANTITY = Integer.valueOf(queryForm.getRows());
		    }else{
		    	QUANTITY = 5;
		    }
		    	
			List<IndexedItem> listIndexedItem = new ArrayList<IndexedItem>();		
			String solrUrl = getSolrBrand(queryForm) ;
			ItemSolrDao itemSolrDao = getItemSolrDao(solrUrl);
			ModelAndView mv =  new ModelAndView("resultado");
			mv.addObject("fields",getFields());			
			String[] fieldsArray = queryForm.getFields();
			listIndexedItem = getItem(itemSolrDao,solrUrl,queryForm,queryFormPartner,fieldsArray);
			model.addAttribute("idList",listIndexedItem);
			if(fieldsArray.length > 0){
				model.addAttribute("itemList",toJson(listIndexedItem,fieldsArray));
			}else{
				model.addAttribute("itemList",listIndexedItem);
			}
			/*model.addAttribute("itemList",toJson(listIndexedItem,fieldsArray));*/
			model.addAttribute("itemList",listIndexedItem);
			model.addAttribute("link",getLink(queryForm.getBrand()));
			model.addAttribute("size",listIndexedItem.size());
			model.addAttribute("solrLink",getSolrBrand(queryForm)+"/idxItem/select?q=itemId%3A");
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

	private String toJson(List<IndexedItem> listIndexedItem, String[] fieldsArray)
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
	


	private String getLink(String marca) {
		if(marca.equalsIgnoreCase("hml"))
			return "http://hml.www.americanas.com.br/produto/";
		return "http://www."+marca+".com.br/produto/";
	}
	
	
	private String getSolrBrand(QueryForm queryForm) {
		if(queryForm.getBrand().equals("submarino")){
			brandStart=20000;
			return "http://10.13.147.14:8080/solr" ;
		}else if(queryForm.getBrand().equals("americanas")){
			brandStart=100000;
			return "http://10.13.51.14:8080/solr";
		}else if(queryForm.getBrand().equals("shoptime")){
			brandStart=50000;
			return "http://10.13.67.14:8080/solr";
		}else if(queryForm.getBrand().equals("soubarato")){
			brandStart=30000;
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
	
	
	
	private List<IndexedItem> getItem(ItemSolrDao itemSolrDao,String solrUrl, QueryForm queryForm, QueryFormPartner queryFormPartner, String[] fieldsArray) throws SolrServerException, IOException{
		StringBuffer queryString= new StringBuffer();
		if(StringUtils.isNotEmpty(System.getProperty("query")))
			queryString.append(System.getProperty("query"));
		String id = queryForm.getId();
		String type = queryForm.getType();
		if(StringUtils.isNotEmpty(id) ) {
			queryString.append("itemId:("+id+")");
			return getItensById(itemSolrDao, queryString,fieldsArray);
		}else if(queryForm.getFashion().equalsIgnoreCase("true")){
			String stock = queryForm.getStock();						
			queryString.append("itemStock:"+stock);	
			return getFashionList(itemSolrDao, queryString,queryForm.getNumSkus(),fieldsArray);
		}else if(queryForm.getKit().equalsIgnoreCase("true")){
			String stock = queryForm.getStock();						
			queryString.append("itemStock:"+stock);	
			return getKits(itemSolrDao, queryString,queryForm.getNumSkus(),fieldsArray);
		}else if (type.equals("b2w")){			
			queryString.append(getQueryType(type));
			String stock = queryForm.getStock();						
			queryString.append(" AND itemStock:"+stock);		
			return getItens(itemSolrDao, queryString,QUANTITY,getRandom(),fieldsArray);
			
		}else {
			Integer numPartner = getNumPartner();
			queryString.append(getQueryType(type));	
			String stock = queryForm.getStock();						
			queryString.append(" AND itemStock:"+stock);
			SolrQuery query = new SolrQuery(queryString.toString());
			query.add("rows", "500");	
			List<IndexedItem> listIndexedItems = new ArrayList<IndexedItem>();
			aux=Integer.valueOf(getRandom());
			while(listIndexedItems.size() < QUANTITY){
				List<IndexedItem> indexedItems = getSimpleItens(itemSolrDao, queryString, 500,getIncrement(Integer.valueOf(aux)),fieldsArray);
				String stockPartner = queryFormPartner.getStockPartner();
				if(indexedItems != null && indexedItems.size() > 0){
					MarketPlaceSolrDao marketPlaceDao = getMarketPlaceItemSolrDao(solrUrl);
					for(IndexedItem indexedItem : indexedItems){
						StringBuffer queryStringPartner= new StringBuffer();
						queryStringPartner.append("itemId:"+indexedItem.getId());
						SolrQuery queryPartner = new SolrQuery(queryStringPartner.toString());
						List<IndexedMarketPlaceItem> indexedMarketPlaceItems = marketPlaceDao.query(queryPartner);
						if(indexedMarketPlaceItems !=null && indexedMarketPlaceItems.size() > 0 ){
							for(IndexedMarketPlaceItem indexedMarketPlaceItem : indexedMarketPlaceItems){
								if((indexedMarketPlaceItem.getItemId().toString().equalsIgnoreCase(indexedItem.getItemId().toString()) &&
										indexedMarketPlaceItem.getItemStock() == Boolean.valueOf(stockPartner) && indexedItem.getPartnerList() != null && indexedItem.getPartnerList().size() == numPartner )){
									listIndexedItems.add(indexedItem);
									break;
								}
							}
						}
						if(listIndexedItems.size() >= QUANTITY)
							return listIndexedItems;
					}
				}
			}
			return listIndexedItems;
		}
		
	}

	private List<IndexedItem> getFashionList(ItemSolrDao itemSolrDao,
			StringBuffer queryString, String numSkus, String[] fieldsArray) {
		List<IndexedItem> listIndexedItemsFashion = new ArrayList<IndexedItem>();
		aux=Integer.valueOf(getRandom());
		while(listIndexedItemsFashion.size() < QUANTITY){
			List<IndexedItem> listIndexedItems = getItens(itemSolrDao, queryString, QUANTITY,getIncrement(aux),fieldsArray);
			for(IndexedItem indexedItem : listIndexedItems){
				System.out.println(aux);
				if(isFashion(indexedItem)){
					
					listIndexedItemsFashion.add(indexedItem);
					if(listIndexedItemsFashion.size() == 1){
						return listIndexedItemsFashion;
					}
				}
			}
		}
		
		return null;
	}

	private boolean isFashion(IndexedItem indexedItem) {
		if (indexedItem.getSkuDiffs() != null) {
			return containsColorAndSize(getSkuWithFashionColorsInDiff(indexedItem), getSkuWithFashionSizesInDiff(indexedItem));
		}
		return false;
	}

	private boolean containsColorAndSize(Set<String> countCor, Set<String> countTamanho) {
		return CollectionUtils.containsAny(FashionProperty.COLOR.getCodes(), countCor) && CollectionUtils.containsAny(FashionProperty.SIZE.getCodes(), countTamanho);
	}
	
	private Set<String> getSkuWithFashionColorsInDiff(IndexedItem indexedItem) {
		Set<String> skuFashionColors = new TreeSet<String>();

		for (String singleSku : indexedItem.getSkuDiffs()) {
			String[] singleSkuSplitted = singleSku.split(SEPARATOR);
			if (singleSkuSplitted.length > 1 && FashionProperty.COLOR.getCodes().contains(singleSkuSplitted[1])) {
				skuFashionColors.add(singleSkuSplitted[1]);
			}
		}

		return skuFashionColors;
	}
	private Set<String> getSkuWithFashionSizesInDiff(IndexedItem indexedItem) {
		Set<String> skuFashionSizes = new TreeSet<String>();

		for (String singleSku : indexedItem.getSkuDiffs()) {
			String[] singleSkuSplitted = singleSku.split(SEPARATOR);
			if (singleSkuSplitted.length > 1 && FashionProperty.SIZE.getCodes().contains(singleSkuSplitted[1])) {
				skuFashionSizes.add(singleSkuSplitted[1]);
			}
		}

		return skuFashionSizes;
	}

	private Integer getNumPartner() {
		return Integer.valueOf(StringUtils.isEmpty(queryForm.getNumPartner()) ? "1" : queryForm.getNumPartner());
	}
	
	private String getStart2() {
		return StringUtils.isEmpty(queryForm.getStart()) ? "0" : queryForm.getStart();
	}

	private String getRandom() {
		if(StringUtils.isEmpty(queryForm.getStart())){
			return String.valueOf(1 + (int)(Math.random() * brandStart));
		}else{
			return queryForm.getStart();
		}
	}
	

	private List<IndexedItem> getKits(ItemSolrDao itemSolrDao,
			StringBuffer queryString,String skuQuantity, String[] fieldsArray) {
		Integer skuQty = StringUtils.isEmpty(skuQuantity) ? 1 : Integer.valueOf(skuQuantity);
		List<IndexedItem> listIndexedItemsKit = new ArrayList<IndexedItem>();
		aux=Integer.valueOf(getRandom());
		while(listIndexedItemsKit.size() < QUANTITY){
			queryString.append(" AND isKit:true");
			SolrQuery query = new SolrQuery(queryString.toString());
			query.add("rows", "20");	
			query.add("start",getStart());
			List<IndexedItem> listIndexedItems = itemSolrDao.query(query);
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

	private Map<String, String> getKitGroupList(List<IndexedItem> listIndexedItemsKit) {
		
		for (IndexedItem indexedItem : listIndexedItemsKit) {
			if(indexedItem.getKitItemList() != null){
				for(String kitItem : indexedItem.getKitItemList()){
					String kitInfos[] = kitItem.split("\\^");
					if(!kitInfos[2].trim().isEmpty()){
						kitGroup.put(indexedItem.getId(), kitInfos[2].trim());
					}
				}
			}
		}
		System.out.println(kitGroup);
		return kitGroup;
	}

	private String getStart() {
		if(StringUtils.isEmpty(queryForm.getStart())){			
			if(a == 0 ){
				a = start;
				a =+ 100;
				return String.valueOf(start);
			}else{
				start =  a;
				a = a + 100;
				return String.valueOf(start);
			}
		}else{
			return queryForm.getStart();
		}
		
	}

	private List<IndexedItem> getItensById(ItemSolrDao itemSolrDao,
			StringBuffer queryString, String[] fieldsArray) {
		SolrQuery query = new SolrQuery(queryString.toString());
		if(fieldsArray.length > 0)
			query.addField(StringUtils.join(fieldsArray,","));
		List<IndexedItem> listIndexedItems = itemSolrDao.query(query);
		return listIndexedItems;
	}
	
	private List<IndexedItem> getItens(ItemSolrDao itemSolrDao,
			StringBuffer queryString,Integer quantity, String start, String[] fieldsArray) {
		List<IndexedItem> listIndexedItems;
		SolrQuery query = new SolrQuery(queryString.toString());
		if(StringUtils.isNotEmpty(this.queryForm.getNumSkus())){
			listIndexedItems = getItensB2wBySku(itemSolrDao,queryString,start,fieldsArray,Integer.valueOf(this.queryForm.getNumSkus()));
		}else{
			query.add("rows",String.valueOf(quantity));
			query.add("start",getRandom());
			if(fieldsArray.length > 0)
				query.addField(StringUtils.join(fieldsArray,","));
			//query.addFilterQuery("+(+isFreeBee:false -soldSeparatelly:false -item_property_EXCLUSIVE_B2B:true)");
			listIndexedItems = itemSolrDao.query(query);
		}
		return listIndexedItems;
	}

	private List<IndexedItem> getItensB2wBySku(ItemSolrDao itemSolrDao, StringBuffer queryString, String start2, String[] fieldsArray, Integer skuQuantity) {
		aux=Integer.valueOf(start2);
		List<IndexedItem> listIndexedItemsBysku = new ArrayList<IndexedItem>();
		while(listIndexedItemsBysku.size() < QUANTITY){
				List<IndexedItem> listIndexedItems = getSimpleItens(itemSolrDao, queryString, 500,getIncrement(Integer.valueOf(aux)),fieldsArray);
				for(IndexedItem indexedItem : listIndexedItems){
					if(indexedItem.getSkuList().size() == skuQuantity){
						listIndexedItemsBysku.add(indexedItem);
						if(listIndexedItemsBysku.size() == QUANTITY){
							return listIndexedItemsBysku;
						}
					}
					
				}
			}
			return null;
	}



	private String getIncrement(Integer start2) {
		aux = start2;
		aux=aux+500;
		
		return String.valueOf(aux);
	}

	private List<IndexedItem> getSimpleItens(ItemSolrDao itemSolrDao,
			StringBuffer queryString, int quantity, String start2, String[] fieldsArray) {
		SolrQuery query = new SolrQuery(queryString.toString());		
		query.add("rows",String.valueOf(quantity));
		query.add("start",start2);
		if(fieldsArray.length > 0)
			query.addField(StringUtils.join(fieldsArray,","));
		//query.addFilterQuery("+(+isFreeBee:false -soldSeparatelly:false -item_property_EXCLUSIVE_B2B:true)");
		System.out.println(start2);
		return  itemSolrDao.query(query);
		
		
	}

	private String getQueryType(String type) {
		if(type!=null){
			if(type.equalsIgnoreCase("100"))
				return("isExclusiveMarketPlace:true");
			else if(type.equalsIgnoreCase("misto"))
				return("isMarketPlace:true AND isExclusiveMarketPlace:false");
			else
				return("isMarketPlace:false");
		}
		return "";
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

	public Map<String, String> getKitGroup() {
		return kitGroup;
	}

	public void setKitGroup(Map<String, String> kitGroup) {
		this.kitGroup = kitGroup;
	}
	
	
	
}
