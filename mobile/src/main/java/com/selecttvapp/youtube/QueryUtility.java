package com.selecttvapp.youtube;

import android.util.Log;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class QueryUtility {
	public static Map<String, String> getQueryParams(String query){
		String[] params = query.split("&");  
		Map<String, String> map = new HashMap<String, String>();  
		for (String param : params){  
			String[] token = param.split("=");
			if (token.length > 1){
				String name  = token[0];  
				String value = URLDecoder.decode(token[1]);  
				map.put(name, value); 
			} else{
				Log.w("QueryUtility", "token.length is not greater than 1. : " + (param == null ? "<null>" : param));
			}
		}  
		return map;  
	}
}
