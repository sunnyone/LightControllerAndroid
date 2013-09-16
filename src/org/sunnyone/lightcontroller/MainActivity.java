package org.sunnyone.lightcontroller;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.os.AsyncTask;
import java.net.HttpURLConnection;
import java.net.URL;

import java.io.*;

public class MainActivity extends Activity {

	class AccessCgiTask extends AsyncTask<String, Void, String> {
		private static final String SERVER_URL = "http://192.168.1.1/lightremo/lightremo.cgi?sw=";

		@Override
		protected String doInBackground(String... swParams) {
			HttpURLConnection urlConn = null;
			try {
				URL url = new URL(SERVER_URL + swParams[0]);
				urlConn = (HttpURLConnection)url.openConnection();
				urlConn.connect();
				return urlConn.getResponseMessage();
			} catch (Exception ex) {
				return "Failed to request: " + ex.toString();
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button buttonOn = (Button)findViewById(R.id.button_on);
		buttonOn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AccessCgiTask().execute("on");
			}
		});

		Button buttonOff = (Button)findViewById(R.id.button_off);
		buttonOff.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AccessCgiTask().execute("off");
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
