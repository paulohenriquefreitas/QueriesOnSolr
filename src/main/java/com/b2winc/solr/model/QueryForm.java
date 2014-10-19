package com.b2winc.solr.model;

import java.util.List;

public class QueryForm {
	
	private String id;
	private String stock;
	private List<String> fields;
	private String fashion;
	private String type;
	private String numPartner;
	private String numSkus;
	
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

	public String getFashion() {
		return fashion;
	}

	public void setFashion(String fashion) {
		this.fashion = fashion;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNumPartner() {
		return numPartner;
	}

	public void setNumPartner(String numPartner) {
		this.numPartner = numPartner;
	}

	public String getNumSkus() {
		return numSkus;
	}

	public void setNumSkus(String numSkus) {
		this.numSkus = numSkus;
	}
		

}
