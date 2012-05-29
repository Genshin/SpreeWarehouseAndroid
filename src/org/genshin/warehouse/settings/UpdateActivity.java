package org.genshin.warehouse.settings;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.genshin.warehouse.R;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UpdateActivity extends Activity {
	private Button updateButton;
	private String apkUri = "http://files.genshin.org/warehouse/warehouse-latest.apk";
	private String apkName = "warehouse";
	
	ProgressDialog dlDialog;
	String path = Environment.getExternalStorageDirectory()+ "/download/"; // Path to where you want to save the file
	String cachedir = "";                                       
	  
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);
        
        updateButton = (Button) findViewById(R.id.update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				doUpdate();
			}
        });
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
