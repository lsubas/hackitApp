package com.team.hackit;

import java.io.IOException;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;

import android.app.Activity;
import android.os.AsyncTask;

public class GetUsernameTask extends AsyncTask<Void, Void, String> {
    Activity mActivity;
    String mScope;
    String mEmail;
    String mToken;

    GetUsernameTask(Activity activity, String name, String scope) {
        this.mActivity = activity;
        this.mScope = scope;
        this.mEmail = name;
    }

    /**
     * Executes the asynchronous job. This runs when you call execute()
     * on the AsyncTask instance.
     */
    @Override
    protected String doInBackground(Void... params) {
        try {
            String token = fetchToken();
            if (token != null) {
                return token;
          }
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
    protected String fetchToken() throws IOException {
        try {
            return GoogleAuthUtil.getToken(mActivity, mEmail, mScope);
        } catch (UserRecoverableAuthException userRecoverableException) {
            // GooglePlayServices.apk is either old, disabled, or not present
            // so we need to show the user some UI in the activity to recover.
            ((MainActivity)mActivity).handleException(userRecoverableException);
        } catch (GoogleAuthException fatalException) {
            // Some other type of unrecoverable exception has occurred.
            // Report and log the error as appropriate for your app.
        	System.out.println("Subash Exception"+fatalException.getMessage());
        }
        return null;
    }
    
    protected void onPostExecute(String token) {
    	((MainActivity)mActivity).setmToken(token);
    }
    
}