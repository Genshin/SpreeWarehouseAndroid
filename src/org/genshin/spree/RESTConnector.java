package org.genshin.spree;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.Credentials;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class RESTConnector extends Activity {
	private Context ctx;
	private Boolean initialized;
	private DefaultHttpClient client;
	
	private String server;
	private String apiKey;
	private int port;
	
	public RESTConnector(Context ctx) {
		this.ctx = ctx;
	}

	public String getBaseURL() {
		return "http://" + server + ":" + port;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialized = false;
	}

	public void setup(String server, int port, String apiKey) {
		this.server = server;
		this.port = port;
		this.apiKey = apiKey;
		
		this.client = new DefaultHttpClient();
		
		/*if (user != null) {
			this.credentials = new UsernamePasswordCredentials(user, password);
			this.scope = new AuthScope(server, (int)port);
			this.client.getCredentialsProvider().setCredentials(scope, credentials);
		}*/
		
		this.initialized = true;
	}
	
	public String test() {
		int statusCode = 0;
		String combinedStatus = "unsent";
		Log.d("RESTDEBUG", combinedStatus);
		
		if (!initialized) {
			Log.d("RESTDEBUG", "Uninitialized");
			return "Uninitialized";
		}
		
		try {
			HttpGet getter = new HttpGet("http://" + this.server + ":" + this.port);
			combinedStatus = "Attempting GET to: " + getter.getURI();
			Log.d("RESTDEBUG", combinedStatus);
			HttpResponse response = this.client.execute(getter);
			combinedStatus = "Executed GET";
			Log.d("RESTDEBUG", combinedStatus);
			StatusLine statusLine = response.getStatusLine();
			statusCode = statusLine.getStatusCode();
			combinedStatus = "GET Result: " + String.valueOf(statusCode);
			Log.d("RESTDEBUG", combinedStatus);
			if (statusCode == 200) {
				combinedStatus = "Connection OK";
			}
		} catch (ClientProtocolException e) {
			combinedStatus = "ClientProtocolException: " + e.getMessage();
		} catch (IOException e) {
			combinedStatus = "IOException: " + e.toString();
			Log.d("RESTDEBUG", combinedStatus);
		}
		
		return combinedStatus;
	}
	
	public HttpResponse getResponse(HttpGet getter) {
		
		try {
			HttpResponse response = client.execute(getter);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				return response;
			} else {
				Toast.makeText(ctx, "Response not 200, Status: " + statusCode, Toast.LENGTH_LONG).show();
			}
		} catch (ClientProtocolException e) {
			Toast.makeText(ctx, "ClientProtocolException: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText(ctx, "IOException: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
		}
		
		return null;
	}

	public JSONObject getJSONObject(String targetURL) {
		JSONObject data = new JSONObject();
							
		HttpGet getter = new HttpGet("http://" + this.server + ":" + this.port + "/" + targetURL);
		//Set headers manually because Android doesn't seem to care to
		getter.addHeader("X-Spree-Token", this.apiKey);
		HttpResponse response = this.getResponse(getter);
		if (response == null) {
			Toast.makeText(ctx, "Server did not respond", Toast.LENGTH_LONG).show();
			return data;
		}

		HttpEntity entity = response.getEntity();
		String content;
		try {
			content = EntityUtils.toString(entity);
			data = new JSONObject(content);
		} catch (ParseException e) {
			Toast.makeText(ctx, "ParseException: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText(ctx, "IOException: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
		} catch (JSONException e) {
			Toast.makeText(ctx, "JSON parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
		}

		return data;
	}
	
	public JSONArray getJSONArray(String targetURL) {
		JSONArray data = new JSONArray();
							
		HttpGet getter = new HttpGet("http://" + this.server + ":" + this.port + "/" + targetURL);
		//Set headers manually because Android doesn't seem to care to
		getter.addHeader("X-Spree-Token", this.apiKey);
		HttpResponse response = this.getResponse(getter);
		if (response == null) {
			Toast.makeText(ctx, "Server did not respond", Toast.LENGTH_LONG).show();
			return data;
		}

		HttpEntity entity = response.getEntity();
		String content;
		try {
			content = EntityUtils.toString(entity);
			data = new JSONArray(content);
		} catch (ParseException e) {
			Toast.makeText(ctx, "ParseException: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText(ctx, "IOException: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
		} catch (JSONException e) {
			Toast.makeText(ctx, "JSON parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
		
		return data;
	}
	
	public int putWithArgs(String targetURL, ArrayList<NameValuePair> pairs) {
		int statusCode = 0;
		
		try {
			HttpPut put = new HttpPut("http://" + this.server + ":" + this.port + "/" + targetURL);
			//Set headers manually because Android doesn't seem to care to
			put.addHeader("X-Spree-Token", this.apiKey);
			if (pairs != null) {
				put.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
				Log.d("RESTConnector.putWithArgs", "added pairs");
			}
			HttpResponse response = client.execute(put);
			StatusLine statusLine = response.getStatusLine();
			statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				String content = EntityUtils.toString(entity);
			} else {
				Log.d("RESTConnector.genericRequest", "Response not 200, Status: " + statusCode);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return statusCode;
	}
	
	public int genericPut(String targetURL) {
		return putWithArgs(targetURL, null);
	}
}

