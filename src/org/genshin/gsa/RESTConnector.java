package org.genshin.gsa;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;


import android.app.Activity;
import android.net.Credentials;
import android.os.Bundle;

public class RESTConnector extends Activity {
	private Boolean initialized;
	private DefaultHttpClient client; 
	
	private UsernamePasswordCredentials credentials;
	private AuthScope scope;
	private String baseURL;
	private int port;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialized = false;
	}

	public void setup(String baseURL, int port, String user, String password) {
		this.baseURL = baseURL;
		this.port = port;
		
		this.client = new DefaultHttpClient();
		
		if (user != null) {
			this.credentials = new UsernamePasswordCredentials(user, password);
			this.scope = new AuthScope(baseURL, port);
			client.getCredentialsProvider().setCredentials(scope, credentials);
		}
		
		this.initialized = true;
	}
	
	public String test() {
		int statusCode = 0;
		String combinedStatus = "unsent";
		
		if (!initialized)
			return "Uninitialized";
		
		try {
			HttpGet getter = new HttpGet(this.baseURL);
			combinedStatus = "Attempting GET to: " + getter.getURI();
			HttpResponse response = this.client.execute(getter);
			combinedStatus = "Executed GET";
			StatusLine statusLine = response.getStatusLine();
			statusCode = statusLine.getStatusCode();
		} catch (ClientProtocolException e) {
			combinedStatus = "ClientProtocolException: " + e.getMessage();
		} catch (IOException e) {
			combinedStatus = "IOException: " + e.toString();
		}
		
		return combinedStatus;
	}
	
	private Object responseToObject(HttpResponse response, Class<?> cls) {
		Gson gson = new Gson();
		/*HttpEntity entity = response.getEntity();
		InputStream content = entity.getContent();
		Reader reader = new InputStreamReader(content);*/
		Reader reader;
		try {
			reader = new InputStreamReader(response.getEntity().getContent());
			return gson.fromJson(reader, cls);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Object GET(String targetURL, Class<?> containerClass) {
		Object data = null;
		
		HttpGet getter = new HttpGet(this.baseURL + targetURL);
		try {
			HttpResponse response = client.execute(getter);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				data = responseToObject(response, containerClass);
			} else {
				//Log.e(ParseJSON.class.toString(), "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return data;
	}
}

