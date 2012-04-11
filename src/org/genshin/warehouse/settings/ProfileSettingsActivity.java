package org.genshin.warehouse.settings;

import java.util.ArrayList;
import java.util.List;

import org.genshin.warehouse.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ProfileSettingsActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_settings);
        
        ProfileDataSource data = new ProfileDataSource(this);
        data.open();
        
        data.createProfile("test", "ttt", "abcd");
        List<Profile> profiles = data.getAllProfiles();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < profiles.size(); i++) {
        	adapter.add(profiles.get(i).user + "@" + profiles.get(i).server);
        }
        Spinner spinner = (Spinner) findViewById(R.id.profile_spinner);
        spinner.setAdapter(adapter);
        
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                Spinner spinner = (Spinner)parent;
                int item = spinner.getSelectedItemPosition();
                
                //insert values into fields for view/edit
                
            }


			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        	
		});

	}
	
}
