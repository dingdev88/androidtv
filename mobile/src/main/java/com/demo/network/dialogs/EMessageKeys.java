package com.demo.network.dialogs;

public enum EMessageKeys {
	
	DIALOG_BODY("body"),
	
	DIALOG_HEADER("title"),
	
	DIALOG_BTN_ONE("btn1"),
	
	DIALOG_BTN_TWO("btn2");
	
	private String _message_key = "";
	
	EMessageKeys(final String action) {
		
		this._message_key = action;
	}
	
	public String getKey() {
		
		return this._message_key;
	}
}