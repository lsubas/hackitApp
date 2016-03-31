package com.team.hackit;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.team.hackit.service.ServiceHandler;
import com.team.hackit.service.parser.TokenCheckParser;

public class CheckTokenTask extends AsyncTask<Void, Void, Boolean> {
    Activity mActivity;
    String mEmail;

    CheckTokenTask(Activity activity, String name) {
        this.mActivity = activity;
        this.mEmail = name;
    }

    /**
     * Executes the asynchronous job. This runs when you call execute()
     * on the AsyncTask instance.
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            return checkToken();
        } catch (IOException e) {
            // The fetchToken() method handles Google-specific exceptions,
            // so this indicates something went wrong at a higher level.
            // TIP: Check for network connectivity before starting the AsyncTask.
        }
        return false;
    }

    /**
     * Gets an authentication token from Google and handles any
     * GoogleAuthException that may occur.
     */
    protected boolean checkToken() throws IOException {
        try {
        	ServiceHandler sh = new ServiceHandler();
        	ArrayList<NameValuePair>  query= new ArrayList<NameValuePair>();
           	query.add(new BasicNameValuePair("id",mEmail) );
           	String response = sh.makeServiceCall(MainActivity.SERVER_URL+"/checkToken", sh.GET, query);
        	
        	return TokenCheckParser.parse(response);
        } catch (Exception exception) {
            
        	exception.printStackTrace();
        } 
        return false;
    }
    
    protected void onPostExecute(Boolean result) {
    	((MainActivity)mActivity).tokenCheckResult(result);
    }
}
