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

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.UpdateRequest;
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

@Controller
public class HomeController {

	@RequestMapping(value="/")
	public ModelAndView test() throws IOException{
		ModelAndView mav = new ModelAndView("home","query",new QueryForm());
		Map<String,String> phones = new HashedMap();
		IndexedItem indexedItem = new IndexedItem();
		for(Field dados : IndexedItem.class.getDeclaredFields()){;
		 
		 phones.put(dados.getName(),dados.getName());
		
		}
		mav.addObject("phonesMap",phones);
		
		
		
		return mav;
	}
	
	@RequestMapping(value="/test", method=RequestMethod.POST)
	public ModelAndView navegue(@ModelAttribute("query") QueryForm queryForm, Model model) throws IOException, SolrServerException{
		List<IndexedItem> listIndexedItem = new ArrayList<IndexedItem>();
		/*indexedItem.setItemName(query.getId());
		indexedItem.setItemId(Long.valueOf(query.getStock()));*/
		String solrUrl = "http://10.13.51.14:8080/solr" ;
		ItemSolrDao itemSolrDao = getItemSolrDao(solrUrl);
		listIndexedItem = getItem(itemSolrDao,solrUrl,queryForm);
		
		model.addAttribute("listIndexedItem",listIndexedItem);
		ModelAndView mv =  new ModelAndView("home");
		mv.addObject("msg", "paulo marketplace");
		Map<String,String> itens = new HashedMap();
		for(Field dados : IndexedItem.class.getDeclaredFields()){		 
		 itens.put(dados.getName(),dados.getName());		
		}
		mv.addObject("phonesMap",itens);		
		
		
		//pega_produtos_no_solr(itemSolrDao,solrUrl);
		
		
		
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
	
	/*	public static MarketPlaceSolrDao getMarketPlaceItemSolrDao(String solrUrl) throws MalformedURLException{
		CommonSolrTemplate solrTemplate =  new CommonSolrTemplate();
		HttpSolrServer solrServer = new HttpSolrServer(solrUrl+"/idxMarketPlace");
		solrTemplate.setServer(solrServer);
		MarketPlaceSolrDao itemSolrDao = new MarketPlaceSolrDao();
		itemSolrDao.setTemplate(solrTemplate);
		
		return itemSolrDao;
	}*/
	
	private List<IndexedItem> getItem(ItemSolrDao itemSolrDao,String solrUrl, QueryForm queryForm) throws SolrServerException, IOException{
		String str = "";
		if(StringUtils.isNotEmpty(System.getProperty("query")))
				str = System.getProperty("query");
		SolrQuery query = new SolrQuery(str);
		query.addFilterQuery(queryForm.getId());
		/*query.add("fl", "itemId");
		query.add("rows", "100000");*/
		List<IndexedItem> indexedItem = itemSolrDao.query(query);
		System.out.println(indexedItem.size());
		
	/*	if(produtos_sem_preco_no_idxItem != null && produtos_sem_preco_no_idxItem.size() > 0 ){
			MarketPlaceSolrDao marketPlaceDao = getMarketPlaceItemSolrDao(solrUrl);
			
			for(IndexedItem item : produtos_sem_preco_no_idxItem){
				SolrQuery qerymkt = new SolrQuery("+itemId:"+item.getItemId() + " AND partnerStatus:true AND itemStock:true");
				
				qerymkt.add("fl", "partnerStatus");
				qerymkt.add("fl", "itemStock");
				
				List<IndexedMarketPlaceItem> lista_de_produtos_marketplace = marketPlaceDao.query(qerymkt);
				
				if(lista_de_produtos_marketplace == null || lista_de_produtos_marketplace.size() == 0){
					System.out.println("Item sem preço no site: " + item.getItemId());
					if(StringUtils.isNotBlank(System.getProperty("remove")) && System.getProperty("remove").equalsIgnoreCase("true")){
						UpdateRequest req = new UpdateRequest();
						req.deleteById(item.getId());
						req.process(itemSolrDao.getTemplate().getServer());
						System.out.println("Item removido do indice "+item.getId());
					}
				}
			}
		}*/
		return indexedItem;
	}
	
}
