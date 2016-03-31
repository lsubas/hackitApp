package com.team.hackit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.AsyncTask;

import com.team.hackit.model.SearchResult;
import com.team.hackit.service.ServiceHandler;
import com.team.hackit.service.parser.SearchResultParser;
import com.team.hackit.service.parser.TokenCheckParser;

public class GetSearchResultTask extends AsyncTask<Void, Void, List<SearchResult>> {
    Activity mActivity;
    String mEmail;
    String msearchText;
    String mToken;

    GetSearchResultTask(Activity activity, String searchText,String token,String id) {
        this.mActivity = activity;
        this.mEmail = id;
        this.msearchText = searchText;
        this.mToken = token;
    }

    /**
     * Executes the asynchronous job. This runs when you call execute()
     * on the AsyncTask instance.
     */
    @Override
    protected List<SearchResult> doInBackground(Void... params) {
        try {
            return getSearchResult();
        } catch (IOException e) {
            // The fetchToken() method handles Google-specific exceptions,
            // so this indicates something went wrong at a higher level.
            // TIP: Check for network connectivity before starting the AsyncTask.
        }
        return null;
    }

    /**
     * Gets an authentication token from Google and handles any
     * GoogleAuthException that may occur.
     */
    protected List<SearchResult> getSearchResult() throws IOException {
        try {
        	ServiceHandler sh = new ServiceHandler();
        	ArrayList<NameValuePair>  params= new ArrayList<NameValuePair>();
        	params.add(new BasicNameValuePair("id",mEmail) );
        	params.add(new BasicNameValuePair("token",mToken) );
        	params.add(new BasicNameValuePair("search",msearchText) );
           	String response = sh.makeServiceCall(MainActivity.SERVER_URL+"/search", sh.POST, params);
        	
        	return SearchResultParser.parse(response);
        } catch (Exception exception) {
            
        	exception.printStackTrace();
        } 
        return null;
    }
    
    protected void onPostExecute(List<SearchResult> result) {
    	((MainActivity)mActivity).searchResult(result);
    }

}
