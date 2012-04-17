package org.genshin.warehouse.profiles;

import java.util.ArrayList;
import java.util.List;

import org.genshin.warehouse.R;

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
	
    EditText server;
    EditText user;
    EditText password;
	
    //hooks up interface elements to callbacks and events
    private void hookupInterface() {
		profiles.attachToSpinner(spinner);
        
        deleteButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
				profiles.deleteSelectedProfile();
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
    }
    
    private void initViewElements() {
    	server = (EditText) findViewById(R.id.server_input);
        user = (EditText) findViewById(R.id.username_input);
        password = (EditText) findViewById(R.id.password_input);
        
        spinner = (Spinner) findViewById(R.id.profile_spinner);
        
        newButton = (Button) findViewById(R.id.profile_new_button);
        deleteButton = (Button) findViewById(R.id.profile_delete_button);
        saveButton = (Button) findViewById(R.id.profile_save_button);
        testButton = (Button) findViewById(R.id.profile_test_button);
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
		
		selectProfile(0);
	
	}

	private void selectProfile(int position) {
		profiles.selectProfile(position);

        //insert values into fields for view/edit
        server.setText(profiles.selected.server);
        user.setText(profiles.selected.user);
        password.setText(profiles.selected.password);
	}
	
	private void setNew() {
		//set the creatingNew flag
		creatingNew = true;
		
		//TODO set a new entry in the spinner
		server.setText("");
		user.setText("");
		password.setText("");
	}
	
	private void createProfile() {
		profiles.createProfile(server.getText().toString(), user.getText().toString(), password.getText().toString());
		loadProfiles();
	}
	
	private void updateProfile() {
		profiles.updateProfile(profiles.list.get(selectedProfile));
		loadProfiles();
	}
}
