package com.b2winc.solr.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public enum FashionProperty {
	
	PRIMARYCOLOR,
	COLOR,
	SIZE,
	UNKNOWN;

	private List<String> codes = new ArrayList<String>();
	
	public static FashionProperty typeOf(String code){
		
		for(FashionProperty type : FashionProperty.values()){
			if (type.hasCode(code)){
				return type;
			}
		}
		
		return FashionProperty.UNKNOWN;
	}
	
	public boolean hasCode(String code) { return codes.contains(code); }
	
	public void addCodes(String codes) {
		if(codes != null){
			String[] sep = codes.split(",");
			this.codes.addAll(Arrays.asList(sep));
		}
	}
	
	public String getCode(){
		if (this.codes == null || this.codes.isEmpty()) return null;
		return this.codes.get(0);
	}
	
	public List<String> getCodes(){
		return this.codes;
	}
	
	public void setValue(String value) {
		addCodes(value);		
	}

}
