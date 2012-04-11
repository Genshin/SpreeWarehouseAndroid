package org.genshin.warehouse.settings;

import org.genshin.warehouse.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ProfileSettingsActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_settings);
        
        ProfileDataSource data = new ProfileDataSource(this);
        data.open();
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // アイテムを追加します
        adapter.add("red");
        adapter.add("green");
        adapter.add("blue");
        Spinner spinner = (Spinner) findViewById(R.id.profile_spinner);
        // アダプターを設定します
        spinner.setAdapter(adapter);


	}
	
}
