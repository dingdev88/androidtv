package com.demo.network.dialogs;

public enum AppDialogUserActions {
	
	OK(1),
	
	CANCEL(2);
	
	private int action = -1;
	
	AppDialogUserActions(final int action) {
		
		this.action = action;
	}
	
	public int getAction() {
		
		return this.action;
	}
}
