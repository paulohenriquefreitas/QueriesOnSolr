package com.b2winc.solr.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
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
import com.b2winc.solr.model.QueryForm;
import com.b2winc.solr.repository.ItemSolrDao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

	@RequestMapping(value="/busca", method=RequestMethod.POST)
	public ModelAndView navegue(@ModelAttribute("query") QueryForm queryForm, Model model , BindingResult result) throws IOException, SolrServerException, JAXBException{
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
			listIndexedItem = getItem(itemSolrDao,solrUrl,queryForm);
			model.addAttribute("itemList",toJson(listIndexedItem));
			model.addAttribute("idList",listIndexedItem);
			model.addAttribute("link",getLink(queryForm.getBrand()));
			return mv;
		}
			
	}

	private String toJson(List<IndexedItem> listIndexedItem)
			throws IOException, JsonParseException, JsonMappingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(listIndexedItem);
		
		return json;
	}
	


	private Object getLink(String marca) {
		if(marca.equalsIgnoreCase("hml"))
			return "http://hml.www.americanas.com.br/produto/";
		return "http://www."+marca+".com.br/produto/";
	}

	private String getSolrBrand(QueryForm queryForm) {
		String solrUrl = "http://10.13.146.27:8080/solr" ;
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
	
	
	
	private List<IndexedItem> getItem(ItemSolrDao itemSolrDao,String solrUrl, QueryForm queryForm) throws SolrServerException, IOException{
		StringBuffer queryString= new StringBuffer();
		if(StringUtils.isNotEmpty(System.getProperty("query")))
			queryString.append(System.getProperty("query"));
		String id = queryForm.getId();
		List<String> fields = queryForm.getFields();
		if(StringUtils.isNumeric(id) && StringUtils.isNotEmpty(id) ) {
			queryString.append("itemId:"+id);
		}else{
			String type = queryForm.getType();
			queryString.append(getQueryType(type));
			String stock = queryForm.getStock();
			if(stock != null)
				queryString.append("AND itemStock:"+stock);
		}
		SolrQuery query = new SolrQuery(queryString.toString());
		query.add("rows", "10");
		
		if(fields!=null){
			query.add("fl",StringUtils.join(fields, ","));
		}
		List<IndexedItem> indexedItem = itemSolrDao.query(query);	
	
		return indexedItem;
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
