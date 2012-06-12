package org.genshin.spree;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class RESTConnector extends Activity {
	private Context ctx;
	private Boolean initialized;
	private HttpClient client;
	
	private String server;
	private String apiKey;
	private int port;
	
	public RESTConnector(Context ctx) {
		this.ctx = ctx;
	}


	// Attempts to set the protocol header based on port
	// TODO - this should be set by a flag probably
	private String protocolHeader() {
		String protocol = "http://";
		if (this.port ==  443) {
			protocol = "https://";
		}
		
		return protocol;
	}

	public String baseURL() {
		return protocolHeader() + server + ":" + port + "/";
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialized = false;
	}

	private HttpClient getHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        trustStore.load(null, null);
	        
			SSLSocketFactory sf = new AnyCertSSLSocketFactory(trustStore);
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	        
	        HttpParams params = new BasicHttpParams();
	        //HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

	        SchemeRegistry registry = new SchemeRegistry();
	        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	        registry.register(new Scheme("https", sf, 443));

	        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

	        return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
	        return new DefaultHttpClient();	
		}
	}
	
	public void setup(String server, int port, String apiKey) {
		this.server = server;
		this.port = port;
		this.apiKey = apiKey;
		
		this.client = getHttpClient();
		
		this.initialized = true;
	}

	// Set up the Getter with the API token and proper URL
	private HttpGet getGetter(String targetURL) {
		HttpGet getter = new HttpGet(baseURL() + targetURL);
		getter.addHeader("X-Spree-Token", this.apiKey);
		
		return getter;
	}
	
	// Process the Getter and handle response exceptions
	public HttpResponse getResponse(HttpGet getter) {
		
		try {
			HttpResponse response = client.execute(getter);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				return response;
			} else {
				Toast.makeText(ctx, getter.getURI() + "\nResponse not 200, Status: " + statusCode, Toast.LENGTH_LONG).show();
				Log.e("getHttpResponse", statusLine.toString());
			}
		} catch (ClientProtocolException e) {
			Toast.makeText(ctx, "ClientProtocolException: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText(ctx, "IOException: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
		}

		
		// Something went wrong!
		//Toast.makeText(ctx, "Server did not respond", Toast.LENGTH_LONG).show();
		return null;
	}
	
	public String test() {
		int statusCode = 0;
		
		HttpGet getter = getGetter("");
		
		try {
			HttpResponse response;
			response = client.execute(getter);
			StatusLine statusLine = response.getStatusLine();
			statusCode = statusLine.getStatusCode();
			response.getStatusLine();
			if (statusCode == 200) {
				return "OK";
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return "ERROR";
		} catch (IOException e) {
			e.printStackTrace();
			return "ERROR";
		}
		
		
		return "NOTCONNECTED";
	}
	

	public JSONObject getJSONObject(String targetURL) {
		JSONObject data = new JSONObject();
		
		HttpResponse response = this.getResponse(getGetter(targetURL));

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
		
		HttpResponse response = getResponse(getGetter(targetURL));
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
			HttpPut put = new HttpPut(baseURL() + targetURL);
			//Set headers manually because Android doesn't seem to care to
			put.addHeader("X-Spree-Token", this.apiKey);
			if (pairs != null) {
				put.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
				//Log.d("RESTConnector.putWithArgs", "added pairs");
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
	
	public int postWithArgs(String targetURL, ArrayList<NameValuePair> pairs) {
		int statusCode = 0;
		
		try {
			HttpPost post = new HttpPost(baseURL() + targetURL);
			//Set headers manually because Android doesn't seem to care to
			post.addHeader("X-Spree-Token", this.apiKey);
			if (pairs != null) {
				post.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
				Log.d("RESTConnector.putWithArgs", "added pairs");
			}
			HttpResponse response = client.execute(post);
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
	
	public InputStream getStream(String path) {
		InputStream inputStream = null;
		
		HttpResponse response = null;
		try {
			HttpGet getter = getGetter(path);
			response = this.getResponse(getter);
		} catch (Exception e) {
			return null;
		}

		HttpEntity entity = null;
		try {			
			Log.d("getStream", "getting entity");
            entity = response.getEntity();
			Log.d("getStream", "entity obtained");
		} catch (Exception e) {
			// stream was null/could not get entity
			// this porbably means the image is missing/corrupt
			entity = null;
			Log.d("getStream", "entity bad");
		}

		if (entity != null) {
			try {
				inputStream = entity.getContent();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*finally {
				if (inputStream != null) {
					inputStream.close();
				}
				entity.consumeContent();
			}*/
		}
		
		return inputStream;
	}
	
	public class AnyCertSSLSocketFactory extends SSLSocketFactory {
	    SSLContext sslContext = SSLContext.getInstance("TLS");

	    public AnyCertSSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
	        super(truststore);

	        TrustManager tm = new X509TrustManager() {
	            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	            }

	            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	            }

	            public X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }
	        };

	        sslContext.init(null, new TrustManager[] { tm }, null);
	    }

	    @Override
	    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
	        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
	    }

	    @Override
	    public Socket createSocket() throws IOException {
	        return sslContext.getSocketFactory().createSocket();
	    }
	}
}

