package com.b2winc.solr.model;

import java.util.List;

public class QueryForm {
	
	private String id;
	private String stock;
	private List<String> fields;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}
	
	
	
	
	

}
