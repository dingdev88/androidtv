package com.selecttvapp.push;

public class ServerConfig {

	// your FCM Advance Push Admin Panel Url
	//public static final String BASE_URL = "http://indiawebcoders.com/mobileapps/pushadmin";
	public static final String BASE_URL = "http://ec2-54-226-136-197.compute-1.amazonaws.com/arvig/push_admin/";
	//public static final String BASE_URL = "http://www.icanstudioz.com/advance_push";

	//for Multiple App only
	public static final String APP_TYPE = "ADVANCE_PUSH";
	
	//=============== DO NOT CHANGE THIS
	public static final String REGISTRATION_URL = "/user/register"; // Do not change it if you have not customized web Admin panel.
	//===============
}
