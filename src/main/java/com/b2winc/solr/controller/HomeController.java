package com.b2winc.solr.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
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
import com.b2winc.solr.model.QueryForm;
import com.b2winc.solr.model.QueryFormPartner;
import com.b2winc.solr.repository.ItemSolrDao;
import com.b2winc.solr.repository.MarketPlaceSolrDao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import flexjson.JSONSerializer;

@Controller
public class HomeController {

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
			listIndexedItem = getItem(itemSolrDao,solrUrl,queryForm,queryFormPartner);
			String[] fieldsArray = queryForm.getFields();
			model.addAttribute("idList",listIndexedItem);
			model.addAttribute("itemList",toJson(listIndexedItem,fieldsArray));
			model.addAttribute("link",getLink(queryForm.getBrand()));
			model.addAttribute("size",listIndexedItem.size());
			return mv;
		}
			
	}

	private String toJson(List<IndexedItem> listIndexedItem, String[] fieldsArray)
			throws IOException, JsonParseException, JsonMappingException {
		if(fieldsArray.length == 0 ){
			Gson gson = new GsonBuilder() .setPrettyPrinting().create();
			String json = gson.toJson(listIndexedItem);			
			return json;
		}else{
			JSONSerializer postDetailsSerializer = new JSONSerializer().include(fieldsArray).exclude("*").prettyPrint(true);
			return postDetailsSerializer.serialize(listIndexedItem);
		}
	}
	


	private Object getLink(String marca) {
		if(marca.equalsIgnoreCase("hml"))
			return "http://hml.www.americanas.com.br/produto/";
		return "http://www."+marca+".com.br/produto/";
	}

	private String getSolrBrand(QueryForm queryForm) {
		String solrUrl = "http://10.13.147.14:8080/solr" ;
		return solrUrl;
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
		if(StringUtils.isNumeric(id) && StringUtils.isNotEmpty(id) ) {
			queryString.append("itemId:"+id);
			return getItensById(itemSolrDao, queryString);
		}else if (type.equals("b2w")){			
			queryString.append(getQueryType(type));
			String stock = queryForm.getStock();						
			queryString.append(" AND itemStock:"+stock);		
			return getItensById(itemSolrDao, queryString);
		}else {
			queryString.append(getQueryType(type));	
			String stock = queryForm.getStock();						
			queryString.append(" AND itemStock:"+stock);
			SolrQuery query = new SolrQuery(queryString.toString());
			query.add("rows", "500");		
			
			List<IndexedItem> indexedItems = itemSolrDao.query(query);
			String stockPartner = queryFormPartner.getStockPartner();
			if(indexedItems != null && indexedItems.size() > 0){
				List<IndexedItem> listIndexedItems = new ArrayList<IndexedItem>();
				MarketPlaceSolrDao marketPlaceDao = getMarketPlaceItemSolrDao(solrUrl);
				for(IndexedItem indexedItem : indexedItems){
					StringBuffer queryStringPartner= new StringBuffer();
					queryStringPartner.append("itemId:"+indexedItem.getId());
					SolrQuery queryPartner = new SolrQuery(queryStringPartner.toString());
					List<IndexedMarketPlaceItem> indexedMarketPlaceItems = marketPlaceDao.query(queryPartner);
					System.out.println(indexedMarketPlaceItems.size());
					if(indexedMarketPlaceItems !=null && indexedMarketPlaceItems.size() > 0 ){
						for(IndexedMarketPlaceItem indexedMarketPlaceItem : indexedMarketPlaceItems){
							if((indexedMarketPlaceItem.getItemId().toString().equalsIgnoreCase(indexedItem.getItemId().toString()) &&
									indexedMarketPlaceItem.getItemStock() == Boolean.valueOf(stockPartner))){
								listIndexedItems.add(indexedItem);
								break;
							}
						}
					}
					if(listIndexedItems.size() >= 20)
						return listIndexedItems;
				}
			}
		}
		
		return Collections.EMPTY_LIST;
	}

	private List<IndexedItem> getItensById(ItemSolrDao itemSolrDao,
			StringBuffer queryString) {
		SolrQuery query = new SolrQuery(queryString.toString());
		query.add("rows", "3");
		query.addFilterQuery("+(+isFreeBee:false -soldSeparatelly:false -item_property_EXCLUSIVE_B2B:true)");
		List<IndexedItem> listIndexedItems = itemSolrDao.query(query);
		return listIndexedItems;
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
	
	
	
}
