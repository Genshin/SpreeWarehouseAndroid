package org.genshin.warehouse.settings;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.genshin.warehouse.R;
import org.genshin.warehouse.Warehouse;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UpdateActivity extends Activity {
	private Button updateButton;
	private TextView installedVersionText;
	private TextView developmentVersionText;
	
	private BuildInfo installedInfo;
	private BuildInfo developmentInfo;

	private String apkUri = "http://files.genshin.org/warehouse/warehouse-latest.apk";
	private String buildInfoUri = "http://files.genshin.org/warehouse/buildinfo.xml";
	private String apkName = "warehouse";
	
	ProgressDialog dlDialog;
	String path = Environment.getExternalStorageDirectory()+ "/download/"; // Path to where you want to save the file
	String cachedir = "";                                       
	
	
	private void hookupInterface() {
        updateButton = (Button) findViewById(R.id.update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				doUpdate();
			}
        });
        
        installedVersionText = (TextView) findViewById(R.id.installed_version_text);
        installedVersionText.setText(installedInfo.getBuildInfoLine());
        
        developmentVersionText = (TextView) findViewById(R.id.development_version_text);
        developmentVersionText.setText(developmentInfo.getBuildInfoLine());
	}
	
	private void getVersions() {
		installedInfo = new BuildInfo(Integer.parseInt(getString(R.integer.version_major)),
				Integer.parseInt(getString(R.integer.version_minor)),
				getString(R.string.version_code),
				Integer.parseInt(getString(R.integer.last_build)));
		
		
		String content = null;
		
		try {
		    DefaultHttpClient httpClient = new DefaultHttpClient();
		    HttpPost httpPost = new HttpPost(buildInfoUri);
		
		    HttpResponse httpResponse = httpClient.execute(httpPost);
		    HttpEntity httpEntity = httpResponse.getEntity();
		    content = EntityUtils.toString(httpEntity);
		
		} catch (UnsupportedEncodingException e) {
		    
		} catch (MalformedURLException e) {
		    
		} catch (IOException e) {
		    
		}
		
		XmlPullParser xpp = Xml.newPullParser();
		try {
			xpp.setInput(new StringReader(content));
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int evt = XmlPullParser.END_DOCUMENT;
		try {
			evt = xpp.getEventType();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		developmentInfo = new BuildInfo();
		
		String name = "";
		String type = "";
		String text = "";
		while (evt != XmlPullParser.END_DOCUMENT) {
			if (evt == XmlPullParser.START_DOCUMENT) {
			} else if(evt == XmlPullParser.START_TAG) {
				name = xpp.getName();
				type = xpp.getAttributeValue(null, "name");
			} else if(evt == XmlPullParser.TEXT) {
				text = xpp.getText();
			} else if(evt == XmlPullParser.END_TAG) {
				System.out.println(name + ":" + type + ":"+ text);
				if (name.equals("integer")) {
					if (type.equals("version_major")) {
						developmentInfo.majorVersion = Integer.parseInt(text);
					} else if (type.equals("version_minor")) {
						developmentInfo.minorVersion = Integer.parseInt(text);
					} else if (type.equals("last_build")) {
						developmentInfo.lastBuild = Integer.parseInt(text);
					}
				} else if (name.equals("string")) {
					if (type.equals("version_code")) {
						developmentInfo.versionCode = text;
					}
				}
				name = type = text = "";
			}

			try {
				evt = xpp.next();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);
        
        Warehouse.setContext(this);
        getVersions();
        hookupInterface();
        
	}
	
	//derived from http://stackoverflow.com/questions/3953090/install-apk-from-remote-server
	private void doUpdate() {
	    TextView webview = new TextView(this);
	    setContentView(webview);

	    File getcache = this.getCacheDir();
	    cachedir = getcache.getAbsolutePath();

	    dlDialog = new ProgressDialog(this);
	    dlDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    dlDialog.setTitle("Downloading");
	    dlDialog.setMessage("Connecting");
	    dlDialog.show();

	    new Thread(new Runnable() {

	        public void run() {

	            String filePath = path;

	            InputStream is = null;
	            OutputStream os = null;
	            URLConnection URLConn = null;

	            try {
	                URL fileUrl;
	                byte[] buf;
	                int ByteRead = 0;
	                int ByteWritten = 0;
	                fileUrl = new URL(apkUri);

	                URLConn = fileUrl.openConnection();

	                is = (InputStream) URLConn.getInputStream();

	                String fileName = apkUri.substring(apkUri.lastIndexOf("/") + 1);

	                File f = new File(filePath);
	                f.mkdirs();
	                String abs = filePath + fileName;
	                f = new File(abs);                      


	                os = new BufferedOutputStream(new FileOutputStream(abs));

	                buf = new byte[1024];

	                //This loop reads the bytes and updates a progress dialog
	                while ((ByteRead = is.read(buf)) != -1) {

	                    os.write(buf, 0, ByteRead);
	                    ByteWritten += ByteRead;

	                    final int tmpWritten = ByteWritten;
	                    runOnUiThread(new Runnable() {

	                        public void run() {
	                            dlDialog.setMessage(""+tmpWritten+" Bytes");
	                        }

	                    });
	                }

	                runOnUiThread(new Runnable() {

	                    public void run() {
	                        dlDialog.setTitle("Startar");
	                    }

	                });
	                is.close();
	                os.flush();
	                os.close();


	                Thread.sleep(200);

	                dlDialog.dismiss();

	                Intent intent = new Intent(Intent.ACTION_VIEW);
	                intent.setDataAndType(Uri.fromFile(new File(abs)),
	                        "application/vnd.android.package-archive");
	                startActivity(intent);
	                finish();

	            } catch (Exception e) {
	                e.printStackTrace();

	            }

	        }
	    }).start();
	}
}
