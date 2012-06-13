package org.genshin.warehouse.settings;

import org.genshin.warehouse.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutWarehouseActivity extends Activity {
	private Button genshinButton;
	private Button freeStyleButton;
	private Button pixtureButton;
	
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        
        genshinButton = (Button) findViewById(R.id.genshin_link_button);
        genshinButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		openLink("http://genshin.org");
        	}
        });
        
        freeStyleButton = (Button) findViewById(R.id.freestyle_link_button);
        freeStyleButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		openLink("http://fs-hobby.jp");
        	}
        });
        
        pixtureButton = (Button) findViewById(R.id.pixture_link_button);
        pixtureButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		openLink("http://pixture.com");
        	}
        });
        
	 }
	 
	 private void openLink(String url) {
	    Intent openLink = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	    startActivity(openLink);
	 }
}

