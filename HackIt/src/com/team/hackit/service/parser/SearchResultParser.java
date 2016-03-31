package com.team.hackit.service.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.team.hackit.model.SearchResult;

public class SearchResultParser {

static public List<SearchResult> parse(String in){
	    List<SearchResult> searchResultList = new ArrayList<SearchResult>();
		try {
			JSONObject reader = new JSONObject(in);
			JSONArray searchItems =  reader.getJSONArray("items");
			
			for(int i=0; i<searchItems.length();++i){
				JSONObject searchItem = (JSONObject)searchItems.get(i);
				String videoId = searchItem.getJSONObject("id")!= null? searchItem.getJSONObject("id").getString("videoId"): null;
				String title = searchItem.getJSONObject("snippet").getString("title");
				String description = searchItem.getJSONObject("snippet").getString("description");
				String thumbmnail = searchItem.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("default").getString("url");
				searchResultList.add(new SearchResult(videoId,title,description,thumbmnail));
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return searchResultList;
		
	}
}
