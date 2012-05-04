package org.genshin.warehouse.profiles;

import java.util.ArrayList;
import java.util.List;

import org.genshin.warehouse.R;
import org.genshin.warehouse.WarehouseActivity;
import org.genshin.warehouse.WarehouseActivity.resultCodes;
import org.json.JSONException;
import org.json.JSONObject;

import spree.RESTConnector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ProfileSettingsActivity extends Activity {
	private int selectedProfile;
	private Profiles profiles;
	private Boolean creatingNew;
	
	Spinner spinner;
	Button newButton;
	Button saveButton;
	Button deleteButton;
	Button testButton;
	Button scanButton;
	
    EditText server;
    EditText port;
    EditText profileName;
    EditText apiKey;
	
    public static enum resultCodes { scan };
    
    //hooks up interface elements to callbacks and events
    private void hookupInterface() {
		//profiles.attachToSpinner(spinner);
        
        deleteButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		deleteProfile();
            }
		});
        
        
        newButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		setNew();
            }
		});
        
        saveButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		if (creatingNew)
        			createProfile();
        		else
        			updateProfile();
            }
		});

		testButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				testProfile();
			}
		});
		
		scanButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, resultCodes.scan.ordinal());
			}
		});
        
        loadProfiles();
    }
    
    private void initViewElements() {
    	server = (EditText) findViewById(R.id.server_input);
    	port = (EditText) findViewById(R.id.port_input);
        profileName = (EditText) findViewById(R.id.profilename_input);
        apiKey = (EditText) findViewById(R.id.apikey_input);
        
        spinner = (Spinner) findViewById(R.id.profile_spinner);
        
        newButton = (Button) findViewById(R.id.profile_new_button);
        deleteButton = (Button) findViewById(R.id.profile_delete_button);
        saveButton = (Button) findViewById(R.id.profile_save_button);
        testButton = (Button) findViewById(R.id.profile_test_button);
        scanButton = (Button) findViewById(R.id.profile_scan_button);
    }
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_settings);
        
        initViewElements();
        profiles = new Profiles(this);
        hookupInterface();
	}
	

	private void loadProfiles() {
		creatingNew = false;
		
		profiles = new Profiles(this);
		    
	    spinner.setAdapter(profiles.getArrayAdapter());
		
		if (profiles.list.size() == 0)
		{
			setNew();
			return;
		}
		
	
		AdapterView.OnItemSelectedListener selected = new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
	            //Spinner spinner = (Spinner)parent;
				//int item = spinner.getSelectedItemPosition();
				selectProfile(position);
	        }

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub	
			}
		};
		
    	spinner.setOnItemSelectedListener(selected);
		
		this.selectProfile(0);
	
	}

	private void selectProfile(int position) {
		profiles.selectProfile(position);

        //insert values into fields for view/edit
        server.setText(profiles.selected.server);
        profileName.setText(profiles.selected.profileName);
        port.setText(String.valueOf(profiles.selected.port));
        apiKey.setText(profiles.selected.apiKey);
	}
	
	private void setNew() {
		//set the creatingNew flag
		creatingNew = true;
		
		//TODO set a new entry in the spinner
		server.setText("");
		profileName.setText("");
		apiKey.setText("");
	}
	
	private void createProfile() {
		profiles.createProfile(server.getText().toString(), Long.parseLong(port.getText().toString()), profileName.getText().toString(), apiKey.getText().toString());
		loadProfiles();
	}
	
	private void updateProfile() {
		profiles.updateProfile(profiles.list.get(selectedProfile));
		loadProfiles();
	}
	
	private void deleteProfile() {
		profiles.deleteSelectedProfile();
		loadProfiles();
	}

	private void testProfile() {
		RESTConnector connector = new RESTConnector();
		connector.setup(profiles.selected.server, profiles.selected.port, profiles.selected.apiKey);
		String result = connector.test();
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == resultCodes.scan.ordinal()) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                
                // Handle successful scan
                // sample QR code content: {profile: { server: "myserver", port: 3000, key: "012bf0bcf3dbf13d66db2119b3cb19cd187c235cb5618ccb" }}
                try {
					JSONObject containerObj = new JSONObject(contents);
					JSONObject profileObj = containerObj.getJSONObject("profile");
					server.setText(profileObj.getString("server"));
			    	port.setText("" + profileObj.getInt("port"));
			        apiKey.setText(profileObj.getString("key"));
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(this, "Invalid Profile Code", Toast.LENGTH_LONG).show();
				}
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            	Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }
}
