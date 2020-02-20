package com.demo.network.common;

public enum ImageType {
	
	JPG(".jpg"), PNG(".png");
	
	private ImageType(String value) {
		
		this.value = value;
	}
	
	private String value;
	
	public String value() {
		
		return value;
	}
}
