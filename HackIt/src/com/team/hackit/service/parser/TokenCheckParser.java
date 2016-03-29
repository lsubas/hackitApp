package com.team.hackit.service.parser;


import org.json.JSONException;
import org.json.JSONObject;

public class TokenCheckParser {

	static public boolean parse(String in){
		
		try {
			JSONObject reader = new JSONObject(in);
			return reader.getBoolean("tokenPresent");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
		
	}
}
