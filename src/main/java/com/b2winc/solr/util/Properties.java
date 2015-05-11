package com.b2winc.solr.util;

import com.b2winc.solr.model.QueryForm;

public final class Properties {
	
	public static String getBrandSolrUrl(QueryForm queryForm){
			if(queryForm.getBrand().equals("submarino")){
				return System.getProperty("suba");
			}else if(queryForm.getBrand().equals("americanas")){
				return System.getProperty("acom");
			}else if(queryForm.getBrand().equals("shoptime")){
				return System.getProperty("shop");
			}else if(queryForm.getBrand().equals("soubarato")){
				return System.getProperty("soub");
			}else{
				return System.getProperty("homolog");
			}
		
	}

	public static String getFashionErpDepId() {
		return System.getProperty("fashionErpDepId") != null ? System.getProperty("fashionErpDepId")
				: "10009073 10009074 10009075 10009076 10009077 10009078 10009079 10009069 10009088";
	}
}
