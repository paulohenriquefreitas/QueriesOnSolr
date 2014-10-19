package com.b2winc.solr.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.ideais.metasolr.template.CommonSolrTemplate;

import com.b2w.catalogbackendcommons.index.IndexedItem;
import com.b2w.catalogbackendcommons.index.IndexedMarketPlaceItem;
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
		Map<String,String> fields = new HashedMap();
		for(Field dados : IndexedItem.class.getDeclaredFields()){
		 		 fields.put(dados.getName(),dados.getName());		
		}
		return fields;
	}

	@RequestMapping(value="/busca", method=RequestMethod.POST)
	public ModelAndView navegue(@ModelAttribute("query") QueryForm queryForm, Model model) throws IOException, SolrServerException, JAXBException{
		List<IndexedItem> listIndexedItem = new ArrayList<IndexedItem>();
			/*indexedItem.setItemName(query.getId());
		indexedItem.setItemId(Long.valueOf(query.getStock()));*/
		String solrUrl = "http://10.13.146.27:8080/solr" ;
		ItemSolrDao itemSolrDao = getItemSolrDao(solrUrl);
		listIndexedItem = getItem(itemSolrDao,solrUrl,queryForm);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(listIndexedItem);
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
		Object json2 = mapper.readValue(json, Object.class);
		model.addAttribute("itemList",mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json2));
		ModelAndView mv =  new ModelAndView("home");
		System.out.println(json);
				
		mv.addObject("fields",getFields());		
		
		
		
		
		
		
		return mv;
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
		String str = "";
		if(StringUtils.isNotEmpty(System.getProperty("query")))
				str = System.getProperty("query");
		SolrQuery query = new SolrQuery(str);
		String id = queryForm.getId();
		if(!id.isEmpty()){
			query = new SolrQuery("itemId:"+id);
		}
		//query.addFilterQuery(queryForm.getId());
	/*	query.add("fl", "itemId");
		query.add("rows", "10");*/
		List<IndexedItem> indexedItem = itemSolrDao.query(query);
		System.out.println(indexedItem.get(0).getId());
		
	
		return indexedItem;
	}
	
	
	
}
