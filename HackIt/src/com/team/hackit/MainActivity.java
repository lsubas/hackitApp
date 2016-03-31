package com.team.hackit;

import java.util.List;

import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.team.hackit.model.SearchResult;
import com.team.hackit.viewadapter.SearchListAdapter;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

    public static String SERVER_URL = "http://10.78.220.140:80";
    private static final String SERVER_CLIENT_ID = "534895897275-k9242i3q768bd9ndf8048vurf6pjkslq.apps.googleusercontent.com";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pickUserAccount();
		
		Button searchButton = (Button) findViewById(R.id.button);

		 OnClickListener oclBtnOk = new OnClickListener() {
		       @Override
		       public void onClick(View v) {
		    	   EditText searchText = (EditText)findViewById(R.id.editText);
		    	   new GetSearchResultTask(MainActivity.this,searchText.getText().toString(),mToken, mEmail ).execute();
		       }
		     };
		 searchButton.setOnClickListener(oclBtnOk);
		 ListView list=(ListView)findViewById(R.id.list);
		 list.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	static final int REQUEST_CODE_PICK_ACCOUNT = 1000;

	private void pickUserAccount() {
	    String[] accountTypes = new String[]{"com.google"};
	    Intent intent = AccountPicker.newChooseAccountIntent(null, null,
	            accountTypes, true, null, null, null, null);
	    startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
	}
	
	
	String mEmail; // Received from newChooseAccountIntent(); passed to getToken()
	String mToken; 

	public void setmToken(String token) {
		this.mToken = token;
		System.out.println("Subash Token======"+token +"  Email===="+mEmail);
	}

	public void tokenCheckResult(boolean result) {
		if (!result){
			Toast.makeText(this, "Token Not Present", Toast.LENGTH_SHORT).show();
			new GetUsernameTask(MainActivity.this, mEmail, SERVER_SCOPE).execute();
		}
		else {
			Toast.makeText(this, "Token Present", Toast.LENGTH_SHORT).show();
		}
	}

	public void searchResult(List<SearchResult> result) {
		
		SearchListAdapter adapter = new SearchListAdapter(this,result);
		ListView list=(ListView)findViewById(R.id.list);
		//ListView list = new ListView(this);

		list.setAdapter(adapter);
		//new AlertDialog.Builder(MainActivity.this).setView(list).setTitle("Search Result").show();
		list.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
	        // Receiving a result from the AccountPicker
	        if (resultCode == RESULT_OK) {
	            mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
	            // With the account name acquired, go get the auth token
	            getUsername(false);
	        } else if (resultCode == RESULT_CANCELED) {
	            // The account picker dialog closed without selecting an account.
	            // Notify users that they must pick an account to proceed.
	            Toast.makeText(this, "Please pick an account", Toast.LENGTH_SHORT).show();
	        }
	    }
	    else if ( requestCode == REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR) {
	        // Receiving a result from the AccountPicker
	        if (resultCode == RESULT_OK) {
	           // With the account name acquired, go get the auth token
	            getUsername(true);
	        } else if (resultCode == RESULT_CANCELED) {
	            // The account picker dialog closed without selecting an account.
	            // Notify users that they must pick an account to proceed.
	            Toast.makeText(this, "User denied access", Toast.LENGTH_SHORT).show();
	        }

	    }
	    // Handle the result from exceptions
	}
	
	
	static String SERVER_SCOPE = "oauth2:server:client_id:"+SERVER_CLIENT_ID+":api_scope:https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/youtube";

	/**
	 * Attempts to retrieve the username.
	 * If the account is not yet known, invoke the picker. Once the account is known,
	 * start an instance of the AsyncTask to get the auth token and do work with it.
	 */
	private void getUsername(boolean skipTokenCheck) {
	    if (mEmail == null) {
	        pickUserAccount();
	    } else if (!skipTokenCheck){
	    	 new CheckTokenTask(MainActivity.this, mEmail).execute();
	    } else {
	    	new GetUsernameTask(MainActivity.this, mEmail, SERVER_SCOPE).execute();
	    }
	}
	
	static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1001;

	/**
	 * This method is a hook for background threads and async tasks that need to
	 * provide the user a response UI when an exception occurs.
	 */
	public void handleException(final Exception e) {
	    // Because this call comes from the AsyncTask, we must ensure that the following
	    // code instead executes on the UI thread.
	    runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	            if (e instanceof GooglePlayServicesAvailabilityException) {
	                // The Google Play services APK is old, disabled, or not present.
	                // Show a dialog created by Google Play services that allows
	                // the user to update the APK
	                int statusCode = ((GooglePlayServicesAvailabilityException)e)
	                        .getConnectionStatusCode();
	                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode,
	                        MainActivity.this,
	                        REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
	                dialog.show();
	            } else if (e instanceof UserRecoverableAuthException) {
	                // Unable to authenticate, such as when the user has not yet granted
	                // the app access to the account, but the user can fix this.
	                // Forward the user to an activity in Google Play services.
	                Intent intent = ((UserRecoverableAuthException)e).getIntent();
	                startActivityForResult(intent,
	                        REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
	            }
	        }
	    });
	}
	
	
}

