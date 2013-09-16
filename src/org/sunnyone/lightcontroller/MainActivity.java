package org.sunnyone.lightcontroller;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.net.HttpURLConnection;
import java.net.URL;

import java.io.*;

public class MainActivity extends Activity {

	class AccessCgiTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urlStrings) {
			HttpURLConnection urlConn = null;
			String urlString = urlStrings[0];
			try {
				URL url = new URL(urlString);
				urlConn = (HttpURLConnection)url.openConnection();
				urlConn.connect();
				return urlConn.getResponseMessage();
			} catch (Exception ex) {
				return "Failed to access " + urlString + ": " + ex.toString();
			} finally {
				if (urlConn != null)
					urlConn.disconnect();
			}
		}

		@Override
        protected void onPostExecute(String response) {
			Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
		}
	}

	private String getLightRemoUrl(Context context, String swParam) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		String lightRemoUrl = pref.getString("lightremo_url", "");
		return lightRemoUrl + "?sw=" + swParam;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button buttonOn = (Button)findViewById(R.id.button_on);
		buttonOn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String lightRemoUrl = getLightRemoUrl(MainActivity.this, "on");
				new AccessCgiTask().execute(lightRemoUrl);
			}
		});

		Button buttonOff = (Button)findViewById(R.id.button_off);
		buttonOff.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String lightRemoUrl = getLightRemoUrl(MainActivity.this, "off");
				new AccessCgiTask().execute(lightRemoUrl);
			}
		});

		Button buttonClose = (Button)findViewById(R.id.button_close);
		buttonClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.this.finish();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_settings:
        	Intent intent = new Intent(this, PrefActivity.class);
        	startActivityForResult(intent, 0);

            break;
        default:
            return super.onOptionsItemSelected(item);
        }
        return false;
    }


}
