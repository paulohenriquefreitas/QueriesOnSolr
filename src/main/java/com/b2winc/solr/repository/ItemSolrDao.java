package com.b2winc.solr.repository;

import java.net.MalformedURLException;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

import br.com.ideais.metasolr.dao.AbstractSolrDao;
import br.com.ideais.metasolr.template.CommonSolrTemplate;

import com.b2w.catalogbackendcommons.index.IndexedItem;

public class ItemSolrDao extends AbstractSolrDao<IndexedItem> {
	
	public static ItemSolrDao getItemSolrDao(String solrUrl) throws MalformedURLException{
		CommonSolrTemplate solrTemplate =  new CommonSolrTemplate();
		HttpSolrServer solrServer = new HttpSolrServer(solrUrl+"/idxItem");
		solrTemplate.setServer(solrServer);
		ItemSolrDao itemSolrDao = new ItemSolrDao();
		itemSolrDao.setTemplate(solrTemplate);
		
		return itemSolrDao;
	}
}
